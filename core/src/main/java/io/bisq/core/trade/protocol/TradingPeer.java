/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.core.trade.protocol;

import io.bisq.common.app.Version;
import io.bisq.common.persistance.Persistable;
import io.bisq.protobuffer.payload.btc.RawTransactionInput;
import io.bisq.protobuffer.payload.crypto.PubKeyRing;
import io.bisq.protobuffer.payload.payment.PaymentAccountPayload;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

@Slf4j
public final class TradingPeer implements Persistable {
    // That object is saved to disc. We need to take care of changes to not break deserialization.
    private static final long serialVersionUID = Version.LOCAL_DB_VERSION;

    // Mutable
    private String accountId;
    private PaymentAccountPayload paymentAccountPayload;
    // private Coin payoutAmount;
    private String payoutAddressString;
    // private byte[] signature;
    private String contractAsJson;
    private String contractSignature;
    private byte[] signature;
    private PubKeyRing pubKeyRing;
    private byte[] multiSigPubKey;
    private List<RawTransactionInput> rawTransactionInputs;
    private long changeOutputValue;
    @Nullable
    private String changeOutputAddress;


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor, initialization
    ///////////////////////////////////////////////////////////////////////////////////////////

    public TradingPeer() {
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            in.defaultReadObject();
        } catch (Throwable t) {
            log.warn("Cannot be deserialized." + t.getMessage());
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getter/Setter for Mutable objects
    ///////////////////////////////////////////////////////////////////////////////////////////

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public byte[] getMultiSigPubKey() {
        return multiSigPubKey;
    }

    public void setMultiSigPubKey(byte[] multiSigPubKey) {
        this.multiSigPubKey = multiSigPubKey;
    }

    public PaymentAccountPayload getPaymentAccountPayload() {
        return paymentAccountPayload;
    }

    public void setPaymentAccountPayload(PaymentAccountPayload paymentAccountPayload) {
        this.paymentAccountPayload = paymentAccountPayload;
    }


    public String getPayoutAddressString() {
        return payoutAddressString;
    }

    public void setPayoutAddressString(String payoutAddressString) {
        this.payoutAddressString = payoutAddressString;
    }

    public String getContractAsJson() {
        return contractAsJson;
    }

    public void setContractAsJson(String contractAsJson) {
        this.contractAsJson = contractAsJson;
    }

    public String getContractSignature() {
        return contractSignature;
    }

    public void setContractSignature(String contractSignature) {
        this.contractSignature = contractSignature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PubKeyRing getPubKeyRing() {
        return pubKeyRing;
    }

    public void setPubKeyRing(PubKeyRing pubKeyRing) {
        this.pubKeyRing = pubKeyRing;
    }

    public void setRawTransactionInputs(List<RawTransactionInput> rawTransactionInputs) {
        this.rawTransactionInputs = rawTransactionInputs;
    }

    public List<RawTransactionInput> getRawTransactionInputs() {
        return rawTransactionInputs;
    }

    public void setChangeOutputValue(long changeOutputValue) {
        this.changeOutputValue = changeOutputValue;
    }

    public long getChangeOutputValue() {
        return changeOutputValue;
    }

    public void setChangeOutputAddress(@Nullable String changeOutputAddress) {
        this.changeOutputAddress = changeOutputAddress;
    }

    @Nullable
    public String getChangeOutputAddress() {
        return changeOutputAddress;
    }
}
