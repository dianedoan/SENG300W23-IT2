package com.autovend.software;

import com.autovend.Card;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.external.CardIssuer;

import java.math.BigDecimal;
import java.util.Calendar;

public class PaywithDebit {

    private SelfCheckoutStation scs;
    private BigDecimal amountDue;
    private CardIssuer bank;
    private String mode;
    public PaywithDebit(SelfCheckoutStation scs ){
        this.scs = scs;
        scs.cardReader.enable();
        amountDue = new CustomerIO().getAmount();
        bank = new CardIssuer("PayDebit");

    }


    class PayCardReaderObserver implements CardReaderObserver{

        @Override
        public void reactToCardInsertedEvent(CardReader reader) {
            mode = "insert";
        }



        @Override
        public void reactToCardTappedEvent(CardReader reader) {
            mode = "tap";
        }

        @Override
        public void reactToCardSwipedEvent(CardReader reader) {
            mode = "swipe";

        }

        @Override
        public void reactToCardDataReadEvent(CardReader reader, Card.CardData data) {
//            bank.addCardData(data.getNumber(), data.getCardholder(), Calendar.getInstance(), data.getCVV(), amountDue);
            int feedback = bank.authorizeHold(data.getNumber(), amountDue);
            if (feedback == -1){

            }
        }

        @Override
        public void reactToCardRemovedEvent(CardReader reader) {

        }

        @Override
        public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }

        @Override
        public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

        }
    }
}
