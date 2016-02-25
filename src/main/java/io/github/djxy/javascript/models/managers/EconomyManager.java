package io.github.djxy.javascript.models.managers;

import io.github.djxy.javascript.models.Script;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-02-20.
 */
public class EconomyManager {

    private final EconomyService economyService;
    private final Script script;

    public EconomyManager(Script script) {
        this.script = script;

        if(script.getGame().getServiceManager().provide(EconomyService.class).isPresent()){
            economyService = script.getGame().getServiceManager().provide(EconomyService.class).get();

            script.addVariable("economyManager", this);
        }
        else
            economyService = null;
    }

    public Currency getCurrency(){
        return economyService.getDefaultCurrency();
    }

    public List<Currency> getCurrencies(){
        return new ArrayList<>(economyService.getCurrencies());
    }

    public EconomyService getEconomyService() {
        return economyService;
    }

    public void createAccount(Player player){
        economyService.createAccount(player.getUniqueId());
    }

    public void createAccount(String name){
        economyService.createVirtualAccount(name);
    }

    public Double getBalance(Player player){
        return getBalance(player, economyService.getDefaultCurrency());
    }

    public void addMoney(Player player, double amount){
        addMoney(player, economyService.getDefaultCurrency(), amount);
    }

    public void setMoney(Player player, double amount){
        setMoney(player, economyService.getDefaultCurrency(), amount);
    }

    public void removeMoney(Player player, double amount){
        removeMoney(player, economyService.getDefaultCurrency(), amount);
    }

    public Double getBalance(Player player, Currency currency){
        if(economyService.getAccount(player.getUniqueId()).isPresent())
            return economyService.getAccount(player.getUniqueId()).get().getBalance(currency).doubleValue();

        return null;
    }

    public void addMoney(Player player, Currency currency, double amount){
        if(economyService.getAccount(player.getUniqueId()).isPresent())
            economyService.getAccount(player.getUniqueId()).get().deposit(currency, new BigDecimal(amount), Cause.of(script));
    }

    public void setMoney(Player player, Currency currency, double amount){
        if(economyService.getAccount(player.getUniqueId()).isPresent())
            economyService.getAccount(player.getUniqueId()).get().setBalance(currency, new BigDecimal(amount), Cause.of(script));
    }

    public void removeMoney(Player player, Currency currency, double amount){
        if(economyService.getAccount(player.getUniqueId()).isPresent())
            economyService.getAccount(player.getUniqueId()).get().withdraw(currency, new BigDecimal(amount), Cause.of(script));
    }

    public Double getBalance(String name){
        return getBalance(name, economyService.getDefaultCurrency());
    }

    public void addMoney(String name, double amount){
        addMoney(name, economyService.getDefaultCurrency(), amount);
    }

    public void setMoney(String name, double amount){
        setMoney(name, economyService.getDefaultCurrency(), amount);
    }

    public void removeMoney(String name, double amount){
        removeMoney(name, economyService.getDefaultCurrency(), amount);
    }

    public Double getBalance(String name, Currency currency){
        if(economyService.getAccount(name).isPresent())
            return economyService.getAccount(name).get().getBalance(currency).doubleValue();

        return null;
    }

    public void addMoney(String name, Currency currency, double amount){
        if(economyService.getAccount(name).isPresent())
            economyService.getAccount(name).get().deposit(currency, new BigDecimal(amount), Cause.of(script));
    }

    public void setMoney(String name, Currency currency, double amount){
        if(economyService.getAccount(name).isPresent())
            economyService.getAccount(name).get().setBalance(currency, new BigDecimal(amount), Cause.of(script));
    }

    public void removeMoney(String name, Currency currency, double amount){
        if(economyService.getAccount(name).isPresent())
            economyService.getAccount(name).get().withdraw(currency, new BigDecimal(amount), Cause.of(script));
    }

}
