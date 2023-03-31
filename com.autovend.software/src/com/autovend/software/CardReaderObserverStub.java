/**
 * @author: Abigia Debebe (30134608),
 * @author: Akib Hasan Aryan (30141456),
 * @author: Andy Tran (30125341),
 * @author: Delaram Bahreini Esfahani (30133864),
 * @author: Diane Doan (30052326),
 * @author: Faiyaz Altaf Pranto (30162576),
 * @author: Ishita Chandra (30159580),
 * @author: Nam Nguyen Vu (30154892),
 * @author: River Sanoy (30129508),
 * @author: Ryan Haoping Zheng (30072318),
 * @author: Xinzhou Li (30066080)
 */

package com.autovend.software;

import com.autovend.Card;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;

public class CardReaderObserverStub implements CardReaderObserver{
    public SelfCheckoutMachineLogic	scMachine;
    public AbstractDevice<? extends AbstractDeviceObserver> device = null;

    public CardReaderObserverStub(SelfCheckoutMachineLogic scMachine) {
        this.scMachine = scMachine;
    }

    @Override
    public void reactToCardInsertedEvent(CardReader reader) {
        scMachine.cardType = "insert";
        System.out.println("Card inserted, reading card data...");
    }

    @Override
    public void reactToCardRemovedEvent(CardReader reader) {
        reader.remove();
    }


    @Override
    public void reactToCardTappedEvent(CardReader reader) {
        scMachine.cardType = "tap";
        System.out.println("Card tapped, reading card data...");

    }

    @Override
    public void reactToCardSwipedEvent(CardReader reader) {
        scMachine.cardType = "swipe";
        System.out.println("Card swiped, reading card data...");

    }

    @Override
    public void reactToCardDataReadEvent(CardReader reader, Card.CardData data) {

        scMachine.cardData = data;
    }

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
        this.device = device;

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
        this.device = device;

    }
}
