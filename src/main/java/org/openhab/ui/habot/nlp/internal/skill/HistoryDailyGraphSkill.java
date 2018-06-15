/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.habot.nlp.internal.skill;

import java.util.Set;

import org.eclipse.smarthome.core.items.Item;
import org.openhab.ui.habot.card.CardBuilder;
import org.openhab.ui.habot.nlp.AbstractItemIntentInterpreter;
import org.openhab.ui.habot.nlp.Intent;
import org.openhab.ui.habot.nlp.IntentInterpretation;
import org.openhab.ui.habot.nlp.Skill;
import org.openhab.ui.habot.nlp.internal.ItemNamedAttributesResolver;
import org.osgi.service.component.annotations.Reference;

/**
 * This {@link Skill} is used to reply with a card containing a daily chart of the matching item(s).
 *
 * @author Yannick Schaus
 */
@org.osgi.service.component.annotations.Component(service = Skill.class)
public class HistoryDailyGraphSkill extends AbstractItemIntentInterpreter {

    private CardBuilder cardBuilder;

    @Override
    public String getIntentId() {
        return "get-history-daily";
    }

    @Override
    public IntentInterpretation interpret(Intent intent, String language) {
        IntentInterpretation interpretation = new IntentInterpretation();
        Set<Item> matchedItems = findItems(intent);

        if (matchedItems == null || matchedItems.isEmpty()) {
            interpretation.setAnswer(answerFormatter.getRandomAnswer("answer_nothing_found"));
            interpretation.setHint(answerFormatter.getStandardTagHint(intent.getEntities()));
        } else {
            interpretation.setMatchedItems(matchedItems);

            String period = "D";
            if (intent.getEntities().containsKey("period")) {
                period = intent.getEntities().get("period").concat(period);
            }

            interpretation.setCard(this.cardBuilder.buildChartCard(intent, matchedItems, period));
        }

        interpretation.setAnswer(answerFormatter.getRandomAnswer("info_found_simple"));

        return interpretation;
    }

    @Reference
    protected void setCardBuilder(CardBuilder cardBuilder) {
        this.cardBuilder = cardBuilder;
    }

    protected void unsetCardBuilder(CardBuilder cardBuilder) {
        this.cardBuilder = null;
    }

    @Reference
    protected void setItemNamedAttributesResolver(ItemNamedAttributesResolver itemNamedAttributesResolver) {
        this.itemNamedAttributesResolver = itemNamedAttributesResolver;
    }

    protected void unsetItemNamedAttributesResolver(ItemNamedAttributesResolver itemNamedAttributesResolver) {
        this.itemNamedAttributesResolver = null;
    }
}
