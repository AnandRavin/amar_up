package com.cypherx.soliditytojava;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.1.1.
 */
public final class CYPHERX extends Contract {
    private static final String BINARY = "60606040526003805460ff19166008179055341561001c57600080fd5b60008054600160a060020a03191633600160a060020a0316908117825560035460ff16600a0a6402540be4000260048190559082526006602052604091829020558051908101604052600781527f4359504845525800000000000000000000000000000000000000000000000000602082015260019080516100a29291602001906100f5565b5060408051908101604052600381527f4359580000000000000000000000000000000000000000000000000000000000602082015260029080516100ea9291602001906100f5565b506000600555610190565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013657805160ff1916838001178555610163565b82800160010185558215610163579182015b82811115610163578251825591602001919060010190610148565b5061016f929150610173565b5090565b61018d91905b8082111561016f5760008155600101610179565b90565b610d348061019f6000396000f3006060604052600436106100f05763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde0381146100f2578063095ea7b31461017c57806318160ddd146101b257806323b872dd146101d757806324bce60c146101ff578063313ce5671461022157806342966c681461024a5780636769d1f91461026057806370a082311461027357806379c65068146102925780637b46b80b146102b45780638da5cb5b146102d657806395d89b4114610305578063a9059cbb14610318578063cd4217c11461033a578063dd62ed3e14610359578063f2fde38b1461037e575b005b34156100fd57600080fd5b61010561039d565b60405160208082528190810183818151815260200191508051906020019080838360005b83811015610141578082015183820152602001610129565b50505050905090810190601f16801561016e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561018757600080fd5b61019e600160a060020a036004351660243561043b565b604051901515815260200160405180910390f35b34156101bd57600080fd5b6101c561047a565b60405190815260200160405180910390f35b34156101e257600080fd5b61019e600160a060020a0360043581169060243516604435610480565b341561020a57600080fd5b61019e600160a060020a0360043516602435610605565b341561022c57600080fd5b610234610701565b60405160ff909116815260200160405180910390f35b341561025557600080fd5b61019e60043561070a565b341561026b57600080fd5b6101c56107ce565b341561027e57600080fd5b6101c5600160a060020a03600435166107d4565b341561029d57600080fd5b6100f0600160a060020a03600435166024356107e6565b34156102bf57600080fd5b61019e600160a060020a0360043516602435610888565b34156102e157600080fd5b6102e9610984565b604051600160a060020a03909116815260200160405180910390f35b341561031057600080fd5b610105610993565b341561032357600080fd5b6100f0600160a060020a03600435166024356109fe565b341561034557600080fd5b6101c5600160a060020a0360043516610c16565b341561036457600080fd5b6101c5600160a060020a0360043581169060243516610c28565b341561038957600080fd5b6100f0600160a060020a0360043516610c45565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104335780601f1061040857610100808354040283529160200191610433565b820191906000526020600020905b81548152906001019060200180831161041657829003601f168201915b505050505081565b600080821161044957600080fd5b50600160a060020a033381166000908152600860209081526040808320938616835292905220819055600192915050565b60045481565b600080821161048e57600080fd5b600160a060020a038416600090815260066020526040902054829010156104b457600080fd5b600160a060020a03831660009081526006602052604090205482810110156104db57600080fd5b600160a060020a038085166000908152600860209081526040808320339094168352929052205482111561050e57600080fd5b600160a060020a0384166000908152600660205260409020546105319083610c8f565b600160a060020a0380861660009081526006602052604080822093909355908516815220546105609083610ca3565b600160a060020a038085166000908152600660209081526040808320949094558783168252600881528382203390931682529190915220546105a29083610c8f565b600160a060020a03808616600081815260086020908152604080832033861684529091529081902093909355600580548601905590851691600080516020610ce98339815191529085905190815260200160405180910390a35060019392505050565b6000805433600160a060020a0390811691161461062157600080fd5b600160a060020a0383166000908152600660205260409020548290101561064757600080fd5b6000821161065457600080fd5b600160a060020a0383166000908152600660205260409020546106779083610c8f565b600160a060020a0384166000908152600660209081526040808320939093556007905220546106a69083610ca3565b600160a060020a0384166000818152600760205260409081902092909255907ff97a274face0b5517365ad396b1fdba6f68bd3135ef603e44272adba3af5a1e09084905190815260200160405180910390a250600192915050565b60035460ff1681565b600160a060020a0333166000908152600660205260408120548290101561073057600080fd5b6000821161073d57600080fd5b600160a060020a0333166000908152600660205260409020546107609083610c8f565b600160a060020a0333166000908152600660205260409020556004546107869083610c8f565b600455600160a060020a0333167fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca58360405190815260200160405180910390a2506001919050565b60055481565b60066020526000908152604090205481565b60005433600160a060020a0390811691161461080157600080fd5b600160a060020a0380831660009081526006602052604080822080548501905560048054850190553090921691600080516020610ce98339815191529084905190815260200160405180910390a381600160a060020a031630600160a060020a0316600080516020610ce98339815191528360405190815260200160405180910390a35050565b6000805433600160a060020a039081169116146108a457600080fd5b600160a060020a038316600090815260076020526040902054829010156108ca57600080fd5b600082116108d757600080fd5b600160a060020a0383166000908152600760205260409020546108fa9083610c8f565b600160a060020a0384166000908152600760209081526040808320939093556006905220546109299083610ca3565b600160a060020a0384166000818152600660205260409081902092909255907f2cfce4af01bcb9d6cf6c84ee1b7c491100b8695368264146a94d71e10a63083f9084905190815260200160405180910390a250600192915050565b600054600160a060020a031681565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104335780601f1061040857610100808354040283529160200191610433565b6000610a08610cd6565b833b91506000808411610a1a57600080fd5b600160a060020a03331660009081526006602052604090205484901015610a4057600080fd5b600160a060020a0385166000908152600660205260409020548481011015610a6757600080fd5b600160a060020a033316600090815260066020526040902054610a8a9085610c8f565b600160a060020a033381166000908152600660205260408082209390935590871681522054610ab99085610ca3565b600160a060020a038616600090815260066020526040812091909155831115610bd8575083600160a060020a03811663c0ee0b8a3386856040518463ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018084600160a060020a0316600160a060020a0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610b76578082015183820152602001610b5e565b50505050905090810190601f168015610ba35780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b1515610bc357600080fd5b6102c65a03f11515610bd457600080fd5b5050505b6005805485019055600160a060020a03808616903316600080516020610ce98339815191528660405190815260200160405180910390a35050505050565b60076020526000908152604090205481565b600860209081526000928352604080842090915290825290205481565b60005433600160a060020a03908116911614610c6057600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b6000610c9d83831115610cc7565b50900390565b6000828201610cc0848210801590610cbb5750838210155b610cc7565b9392505050565b801515610cd357600080fd5b50565b602060405190810160405260008152905600ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3efa165627a7a723058202e9e1f78436f85aeddfce4ba7e20f32847e6039be6b1c6e20bed47fb3e0a66c50029";

    private CYPHERX(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private CYPHERX(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._data = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._data = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Burn", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            BurnEventResponse typedResponse = new BurnEventResponse();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BurnEventResponse> burnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Burn", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BurnEventResponse>() {
            @Override
            public BurnEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                BurnEventResponse typedResponse = new BurnEventResponse();
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<FreezeEventResponse> getFreezeEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Freeze", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<FreezeEventResponse> responses = new ArrayList<FreezeEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            FreezeEventResponse typedResponse = new FreezeEventResponse();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<FreezeEventResponse> freezeEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Freeze", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, FreezeEventResponse>() {
            @Override
            public FreezeEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                FreezeEventResponse typedResponse = new FreezeEventResponse();
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<UnfreezeEventResponse> getUnfreezeEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Unfreeze", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<UnfreezeEventResponse> responses = new ArrayList<UnfreezeEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            UnfreezeEventResponse typedResponse = new UnfreezeEventResponse();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnfreezeEventResponse> unfreezeEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Unfreeze", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, UnfreezeEventResponse>() {
            @Override
            public UnfreezeEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                UnfreezeEventResponse typedResponse = new UnfreezeEventResponse();
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents1(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable1(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<TokenFallbackEventResponse> getTokenFallbackEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TokenFallback", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TokenFallbackEventResponse> responses = new ArrayList<TokenFallbackEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TokenFallbackEventResponse typedResponse = new TokenFallbackEventResponse();
            typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TokenFallbackEventResponse> tokenFallbackEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TokenFallback", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TokenFallbackEventResponse>() {
            @Override
            public TokenFallbackEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TokenFallbackEventResponse typedResponse = new TokenFallbackEventResponse();
                typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        Function function = new Function(
                "approve", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        Function function = new Function("totalSupply", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        Function function = new Function(
                "transferFrom", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> freeze(String _freeze, BigInteger _value) {
        Function function = new Function(
                "freeze", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_freeze), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        Function function = new Function("decimals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _value) {
        Function function = new Function(
                "burn", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> soldToken() {
        Function function = new Function("soldToken", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String param0) {
        Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> mintToken(String target, BigInteger mintedAmount) {
        Function function = new Function(
                "mintToken", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(target), 
                new org.web3j.abi.datatypes.generated.Uint256(mintedAmount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> unfreeze(String _unfreeze, BigInteger _value) {
        Function function = new Function(
                "unfreeze", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_unfreeze), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        Function function = new Function("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> freezeOf(String param0) {
        Function function = new Function("freezeOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowance(String param0, String param1) {
        Function function = new Function("allowance", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0), 
                new org.web3j.abi.datatypes.Address(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        Function function = new Function(
                "transferOwnership", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<CYPHERX> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CYPHERX.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<CYPHERX> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CYPHERX.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static CYPHERX load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CYPHERX(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CYPHERX load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CYPHERX(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public String _from;

        public String _to;

        public BigInteger _value;

        public byte[] _data;
    }

    public static class BurnEventResponse {
        public String from;

        public BigInteger value;
    }

    public static class FreezeEventResponse {
        public String from;

        public BigInteger value;
    }

    public static class UnfreezeEventResponse {
        public String from;

        public BigInteger value;
    }

    public static class TransferEventResponse1 {
        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class TokenFallbackEventResponse {
        public String _from;

        public BigInteger _value;

        public byte[] _data;
    }
}
