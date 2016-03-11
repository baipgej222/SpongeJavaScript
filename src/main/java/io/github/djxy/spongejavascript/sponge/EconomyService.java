package io.github.djxy.spongejavascript.sponge;

import io.github.djxy.spongejavascript.Script;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-02-20.
 */
public class EconomyService {

    private final org.spongepowered.api.service.economy.EconomyService economyService;
    private final Script script;

    public EconomyService(Script script) {
        this.script = script;

        if(script.getGame().getServiceManager().provide(org.spongepowered.api.service.economy.EconomyService.class).isPresent()){
            economyService = script.getGame().getServiceManager().provide(org.spongepowered.api.service.economy.EconomyService.class).get();

            script.addVariable("economyService", this);
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

    public org.spongepowered.api.service.economy.EconomyService getEconomyService() {
        return economyService;
    }

    public void createAccount(Player player){
        economyService.getOrCreateAccount(player.getUniqueId());
    }

    public void createAccount(String name){
        economyService.getOrCreateAccount(name);
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
        if(economyService.getOrCreateAccount(player.getUniqueId()).isPresent())
            return economyService.getOrCreateAccount(player.getUniqueId()).get().getBalance(currency).doubleValue();

        return null;
    }

    public void addMoney(Player player, Currency currency, double amount){
        if(economyService.getOrCreateAccount(player.getUniqueId()).isPresent())
            economyService.getOrCreateAccount(player.getUniqueId()).get().deposit(currency, new BigDecimal(amount), Cause.of(NamedCause.source(script)));
    }

    public void setMoney(Player player, Currency currency, double amount){
        if(economyService.getOrCreateAccount(player.getUniqueId()).isPresent())
            economyService.getOrCreateAccount(player.getUniqueId()).get().setBalance(currency, new BigDecimal(amount), Cause.of(NamedCause.source(script)));
    }

    public void removeMoney(Player player, Currency currency, double amount){
        if(economyService.getOrCreateAccount(player.getUniqueId()).isPresent())
            economyService.getOrCreateAccount(player.getUniqueId()).get().withdraw(currency, new BigDecimal(amount), Cause.of(NamedCause.source(script)));
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
        if(economyService.getOrCreateAccount(name).isPresent())
            return economyService.getOrCreateAccount(name).get().getBalance(currency).doubleValue();

        return null;
    }

    public void addMoney(String name, Currency currency, double amount){
        if(economyService.getOrCreateAccount(name).isPresent())
            economyService.getOrCreateAccount(name).get().deposit(currency, new BigDecimal(amount), Cause.of(NamedCause.source(script)));
    }

    public void setMoney(String name, Currency currency, double amount){
        if(economyService.getOrCreateAccount(name).isPresent())
            economyService.getOrCreateAccount(name).get().setBalance(currency, new BigDecimal(amount), Cause.of(NamedCause.source(script)));
    }

    public void removeMoney(String name, Currency currency, double amount){
        if(economyService.getOrCreateAccount(name).isPresent())
            economyService.getOrCreateAccount(name).get().withdraw(currency, new BigDecimal(amount), Cause.of(NamedCause.source(script)));
    }

}
