package uk.org.squirm3.ui.reaction;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import org.springframework.context.MessageSource;

import uk.org.squirm3.engine.ApplicationEngine;
import uk.org.squirm3.model.Configuration;
import uk.org.squirm3.model.Reaction;
import uk.org.squirm3.model.type.ReactionType;
import uk.org.squirm3.model.type.Types;
import uk.org.squirm3.springframework.Messages;

public class ReactionConstructorPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JCheckBox bondedBefore, bondedAfter;
    private JComboBox<ReactionType> aType, bType;
    private JComboBox<?> aState, bState, futureAState, futureBState;
    private JLabel futureAType, futureBType;
    private JButton addReaction;
    
    private JTextField probability;
    
    private final MessageSource messageSource;

    public ReactionConstructorPanel(final ApplicationEngine applicationEngine,
            final MessageSource messageSource, final ImageIcon addIcon) {
    	this.messageSource = messageSource;
        setupLayoutAndBorder();
        final ActionListener updateReactionListener = new UpdateReactionListener();
        setupReactionForm(updateReactionListener);
        setupAddReactionButton(applicationEngine, addIcon);
        updateReactionListener.actionPerformed(null);
        setMaximumSize(getMinimumSize());
    }

    private void setupLayoutAndBorder() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                Messages.localize("reactions.editor", messageSource)));
    }

    private void setupReactionForm(final ActionListener updateReactionListener) {
        final JPanel reactionForm = new JPanel(
                new FlowLayout(FlowLayout.CENTER));
        aType = createTypeComboBox(updateReactionListener, reactionForm);
        aState = createStateComboBox(updateReactionListener, reactionForm);
        bondedBefore = createBondedCheckBox(updateReactionListener,
                reactionForm);
        bType = createTypeComboBox(updateReactionListener, reactionForm);
        bState = createStateComboBox(updateReactionListener, reactionForm);
        probability = createProbabilityText(updateReactionListener, reactionForm);
        createJLabel(" => ", reactionForm);
        futureAType = createJLabel("", reactionForm);
        futureAState = createStateComboBox(updateReactionListener, reactionForm);
        bondedAfter = createBondedCheckBox(updateReactionListener, reactionForm);
        futureBType = createJLabel("", reactionForm);
        futureBState = createStateComboBox(updateReactionListener, reactionForm);
        add(reactionForm);
    }

    private JTextField createProbabilityText(final ActionListener actionListener, JPanel parent) {
    	 final JTextField jText = new JTextField(5);
    	 jText.setText("1");
    	 jText.setToolTipText(Messages.localize("probability.add.tooltip",
                 messageSource));
    	 jText.addKeyListener( new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				addReaction.setText(createReactionFromEditor().toString());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		} );
    	 jText.addActionListener(actionListener);
         parent.add(jText);
         return jText;
	}

	private void setupAddReactionButton(
            final ApplicationEngine applicationEngine,
            final ImageIcon addIcon) {
        addReaction = new JButton(addIcon);
        addReaction.setMargin(new Insets(0, 0, 0, 0));
        addReaction.setToolTipText(Messages.localize("reactions.add.tooltip",
                messageSource));
        addReaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                applicationEngine.addReactions(Collections
                        .singleton(createReactionFromEditor()));
            }
        });
        add(addReaction);
    }

    private Reaction createReactionFromEditor() {
    	
    	BigDecimal probabilityRate = null;
    	try {
    		probabilityRate = new BigDecimal(probability.getText());
    	} catch (Exception e) {
    		probabilityRate = new BigDecimal("1");
    	}
    	
        return new Reaction((ReactionType) aType.getSelectedItem(),
                aState.getSelectedIndex(), bondedBefore.isSelected(),
                (ReactionType) bType.getSelectedItem(),
                bState.getSelectedIndex(), futureAState.getSelectedIndex(),
                bondedAfter.isSelected(), futureBState.getSelectedIndex(),
                probabilityRate);
    }

    private JComboBox<ReactionType> createTypeComboBox(
            final ActionListener actionListener, final JPanel parent) {
        final JComboBox<ReactionType> jComboBox = new JComboBox<>();
        jComboBox.setRenderer(new ReactionTypeListCellRenderer());
        for (final ReactionType reactionType : Types.getReactionTypes()) {
            jComboBox.addItem(reactionType);
        }
        jComboBox.addActionListener(actionListener);
        parent.add(jComboBox);
        return jComboBox;
    }

    private JComboBox<String> createStateComboBox(
            final ActionListener actionListener, final JPanel parent) {
        final JComboBox<String> jComboBox = new JComboBox<>();
        for (int i = 0; i < Configuration.MAX_NUMBER_OF_STATUS; i++) {
            jComboBox.addItem(String.valueOf(i));
        }
        jComboBox.addActionListener(actionListener);
        parent.add(jComboBox);
        return jComboBox;
    }

    private JCheckBox createBondedCheckBox(final ActionListener actionListener,
            final JPanel parent) {
        final JCheckBox jCheckBox = new JCheckBox();
        jCheckBox.addActionListener(actionListener);
        parent.add(jCheckBox);
        return jCheckBox;
    }

    private JLabel createJLabel(final String label, final JPanel parent) {
        final JLabel jLabel = new JLabel(label);
        parent.add(jLabel);
        return jLabel;
    }

    private final class UpdateReactionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            futureAType.setText(toStringIdentifier((ReactionType)aType.getSelectedItem()));
            futureBType.setText(toStringIdentifier((ReactionType)bType.getSelectedItem()));
            addReaction.setText(createReactionFromEditor().toString());
        }
    }

    private static final class ReactionTypeListCellRenderer
            implements
                ListCellRenderer<ReactionType> {
        @Override
        public Component getListCellRendererComponent(
                final JList<? extends ReactionType> list,
                final ReactionType value, final int index,
                final boolean isSelected, final boolean cellHasFocus) {
            return new JLabel(toStringIdentifier(value));
        }
    }

    private static String toStringIdentifier(final ReactionType reactionType) {
        return "" + reactionType.getCharacterIdentifier();
    }

}
