/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.script.sponge;

import io.github.djxy.spongejavascript.script.Script;
import org.spongepowered.api.Sponge;
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

        if(Sponge.getGame().getServiceManager().provide(org.spongepowered.api.service.economy.EconomyService.class).isPresent()){
            economyService = Sponge.getGame().getServiceManager().provide(org.spongepowered.api.service.economy.EconomyService.class).get();

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
