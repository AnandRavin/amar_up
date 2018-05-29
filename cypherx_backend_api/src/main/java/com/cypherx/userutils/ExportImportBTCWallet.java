package com.cypherx.userutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.*;
import org.bitcoinj.core.NetworkParameters;

public class ExportImportBTCWallet {
	  /**
     * Export keys as Base58-encoded strings (compatible with the Bitcoin-Qt client)
     *
     * The keys will be written to the BitcoinWallet.keys file in the following format:
     *   Label: <text>
     *   Time: <creation-time>
     *   Address: <bitcoin-address>
     *   Private: <private-key>
     *
     * @throws      IOException         Unable to create export file
     */
    private void exportPrivateKeys() throws IOException {
        StringBuilder keyText = new StringBuilder(256);
        NetworkParameters Parameters = MainNetParams.get();
        File keyFile = new File(Main.dataPath + Main.fileSeparator+"BitcoinWallet.keys");
        if (keyFile.exists())
            keyFile.delete();
        //
        // Write the keys to BitcoinWallet.keys
        //
        try (BufferedWriter out = new BufferedWriter(new FileWriter(keyFile))) {
            for (ECKey key : Parameters.keys) {
                String address = key.toAddress().toString();
                DumpedPrivateKey dumpedKey = key.getPrivKey();
                keyText.append("Label:");
                keyText.append(key.getLabel());
                keyText.append("\nTime:");
                keyText.append(Long.toString(key.getCreationTime()));
                keyText.append("\nAddress:");
                keyText.append(address);
                keyText.append("\nPrivate:");
                keyText.append(dumpedKey.toString());
                keyText.append("\n\n");
                out.write(keyText.toString());
                keyText.delete(0,keyText.length());
            }
        }
        System.out.println("Keys exported to BitcoinWallet.keys"+ ": Keys Exported");
    }

    /**
     * Import private keys
     *
     * The keys will be read from the BitcoinWallet.keys file.  The keys must be in the format created by
     * exportPrivateKeys().  Blank lines and lines beginning with '#' will be ignored.  Lines containing
     * unrecognized prefixes will also be ignored.
     *
     * @throws      AddressFormatException      Address format is not valid
     * @throws      IOException                 Unable to read file
     * @throws      WalletException             Unable to update database
     */
    private void importPrivateKeys() throws IOException, AddressFormatException, WalletException {
        File keyFile = new File(Main.dataPath +Main.fileSeparator +"BitcoinWallet.keys");
        if (!keyFile.exists()) {
        	System.out.println("BitcoinWallet.keys does not exist"+ "Error");
            return;
        }
        //
        // Read each line from the key file
        //
        try (BufferedReader in = new BufferedReader(new FileReader(keyFile))) {
            String line;
            String importedLabel = "";
            String importedTime = "";
            String importedAddress = "";
            String encodedPrivateKey = "";
            boolean foundKey = false;
            while ((line=in.readLine()) != null) {
                //
                // Remove leading and trailing whitespace
                //
                line = line.trim();
                //
                // Skip comment lines and blank lines
                //
                if (line.length() == 0 || line.charAt(0) == '#')
                    continue;
                int sep = line.indexOf(':');
                if (sep <1 || line.length() == sep+1)
                    continue;
                //
                // Parse the line formatted as "keyword:value".  The following keywords are supported and
                // must appear in the listed order:
                //    Label = Name assigned to the key (may be omitted)
                //    Time = Key creation time (may be omitted)
                //    Address = Bitcoin address for the key (may be omitted)
                //    Private = Private key (must be specified and must be the last line for the key)
                //
                String keyword = line.substring(0, sep);
                String value = line.substring(sep+1);
                switch (keyword) {
                    case "Label":
                        importedLabel = value;
                        break;
                    case "Time":
                        importedTime = value;
                        break;
                    case "Address":
                        importedAddress = value;
                        break;
                    case "Private":
                        encodedPrivateKey = value;
                        foundKey = true;
                        break;
                }
                //
                // Add the key to the wallet and update the bloom filter
                //
                if (foundKey) {
                    DumpedPrivateKey dumpedKey = new DumpedPrivateKey(encodedPrivateKey);
                    ECKey key = dumpedKey.getKey();
                    if (importedAddress.equals(key.toAddress().toString())) {
                        key.setLabel(importedLabel);
                        key.setCreationTime(Long.parseLong(importedTime));
                        if (!Parameters.keys.contains(key)) {
                            Parameters.wallet.storeKey(key);
                            synchronized(Parameters.lock) {
                                boolean added = false;
                                for (int i=0; i<Parameters.keys.size(); i++) {
                                    if (Parameters.keys.get(i).getLabel().compareToIgnoreCase(importedLabel) > 0) {
                                        Parameters.keys.add(i, key);
                                        added = true;
                                        break;
                                    }
                                }
                                if (!added)
                                    Parameters.keys.add(key);
                                Parameters.bloomFilter.insert(key.getPubKey());
                                Parameters.bloomFilter.insert(key.getPubKeyHash());
                            }
                        }
                    } else {
                    	System.out.println(String.format("Address %s does not match imported private key", importedAddress)+ "Error");
                    }
                    //
                    // Reset for the next key
                    //
                    foundKey = false;
                    importedLabel = "";
                    importedTime = "";
                    importedAddress = "";
                    encodedPrivateKey = "";
                }
            }
        }
        System.out.println("Keys imported from BitcoinWallet.keys"+ " : Keys Imported");
    }

}
