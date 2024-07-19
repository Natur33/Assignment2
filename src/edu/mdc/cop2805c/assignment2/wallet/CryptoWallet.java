package edu.mdc.cop2805c.assignment2.wallet;

import java.util.ArrayList;
import java.util.List;

import edu.mdc.cop2805c.assignment2.assets.CryptoCurrencyHolding;

public class CryptoWallet<T> {
    private String niceName;
    private String address;
    @SuppressWarnings("unused")
    private String privateKeyFilename;
    private List<T> holdings;

    public CryptoWallet(String niceName, String address, String privateKeyFilename) {
        this.niceName = niceName;
        this.address = address;
        this.privateKeyFilename = privateKeyFilename;
        this.holdings = new ArrayList<>();
    }

    public void buy(T asset, String fromAddress) {
        holdings.add(asset);
    }

    public void sell(T asset, String toAddress) {
        holdings.remove(asset);
    }

    public void transferTo(T asset, CryptoWallet<T> toWallet) {
        if (holdings.remove(asset)) {
            toWallet.buy(asset, this.address);
        }
    }

    public double getTotalValueInUSD() {
        return holdings.stream()
                .mapToDouble(holding -> {
                    if (holding instanceof CryptoCurrencyHolding) {
                        return ((CryptoCurrencyHolding) holding).getValueInUSD();
                    }
                    return 0.0;
                })
                .sum();
    }

    public String getShortDescription() {
        return String.format("%s: $%.2f", niceName, getTotalValueInUSD());
    }

    public String getLongDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (Address: %s)\n", niceName, address));
        sb.append(String.format("Total Value: $%.2f\n", getTotalValueInUSD()));
        sb.append("Holdings:\n");
        for (T holding : holdings) {
            sb.append(holding.toString()).append("\n");
        }
        return sb.toString();
    }

    public String getNiceName() {
        return niceName;
    }

    public List<T> getHoldings() {
        return holdings;
    }
}