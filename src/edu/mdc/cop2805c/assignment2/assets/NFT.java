package edu.mdc.cop2805c.assignment2.assets;

import edu.mdc.cop2805c.assignment2.base.CryptoAsset;
import edu.mdc.cop2805c.assignment2.base.WalletStorable;

public class NFT extends CryptoAsset implements WalletStorable {
    private String name;
    private String description;
    private String contractAddress;
    private double valueInUSD;

    public NFT(String name, String description, String contractAddress, String blockchainNetwork, double valueInUSD) {
        super(blockchainNetwork);
        this.name = name;
        this.description = description;
        this.contractAddress = contractAddress;
        this.valueInUSD = valueInUSD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public void setValueInUSD(double valueInUSD) {
        this.valueInUSD = valueInUSD;
    }

    @Override
    public double getValueInUSD() {
        return valueInUSD;
    }

    @Override
    public String getShortDescription() {
        return name + ": USD " + String.format("%.2f", valueInUSD);
    }

    @Override
    public String getLongDescription() {
        return String.format("[%s]%s, \n\tDescription:%s, \n\tContract Address:%s, \n\tPrice:%.1f",
                getBlockchainNetwork(), name, description, contractAddress, valueInUSD);
    }

    @Override
    public String toString() {
        return "NFT{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", valueInUSD=" + valueInUSD +
                ", blockchainNetwork='" + getBlockchainNetwork() + '\'' +
                '}';
    }
}

/*// NFT class inherits from CryptoAsset and implements WalletStorable
public class NFT extends CryptoAsset implements WalletStorable {

    // Properties
    private String name;
    private String description;
    private String contractAddress;
    private double valueInUSD;

    // Constructor
    public NFT(String name, String description, String contractAddress, String blockchainNetwork, double valueInUSD) {
        super(blockchainNetwork);
        this.name = name;
        this.description = description;
        this.contractAddress = contractAddress;
        this.valueInUSD = valueInUSD;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public double getValueInUSD() {
        return valueInUSD;
    }

    public void setValueInUSD(double valueInUSD) {
        this.valueInUSD = valueInUSD;
    }

    // Implementing methods from WalletStorable interface
    @Override
    public String getShortDescription() {
        return String.format("%s (%s): $%.2f", name, getBlockchainNetwork(), valueInUSD);
    }

    @Override
    public String getLongDescription() {
        return String.format("Name: %s\nDescription: %s\nContract Address: %s\nBlockchain Network: %s\nValue in USD: $%.2f",
                name, description, contractAddress, getBlockchainNetwork(), valueInUSD);
    }
}*/
