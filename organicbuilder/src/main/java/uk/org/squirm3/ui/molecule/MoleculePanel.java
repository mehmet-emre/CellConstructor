package uk.org.squirm3.ui.molecule;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import org.springframework.context.MessageSource;

import uk.org.squirm3.engine.ApplicationEngine;
import uk.org.squirm3.model.Atom;
import uk.org.squirm3.model.type.ReactionType;
import uk.org.squirm3.springframework.Messages;

public class MoleculePanel extends JPanel {
    
//    private JTextField atomName;
    private JTextField atomSize;
    private JButton atomImage;
    private MessageSource messageSource;
    private JButton addAtom;
    
    private JComboBox<Atom> atomType;
    /**
     * 
     */
    private static final long serialVersionUID = -267585839307547735L;

    public MoleculePanel(final ApplicationEngine applicationEngine, final MessageSource messageSource) {
        this.messageSource = messageSource;
        setupLayoutAndBorder();
        
//        atomName = new JTextField("atom name");
//        add(atomName);
        atomType = applicationEngine.getAtomTypeList();
        atomType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Atom a = (Atom) atomType.getSelectedItem();
                atomSize.setText(Double.toString(a.getSize()));
            }
        });
        add(atomType);
        
        atomSize = new JTextField();
        atomSize.setToolTipText("Set Atom Size");
        add(atomSize);
        
        atomImage = new JButton("future image upload stuff..");
        add(atomImage);
        
        addAtom = new JButton("Edit Atom");
        addAtom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Atom a = (Atom) atomType.getSelectedItem();
                double size = a.getSize();
                try {
                    size = Double.parseDouble(atomSize.getText());
                } finally {
                    if (size <= 0) {
                        size = a.getSize();
                    }
                }
                applicationEngine.setAtomSize(a.getType().toString(), a.getState(), size);
            }
        });
        add(addAtom);
        
    }
    
    private void setupLayoutAndBorder() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                Messages.localize("molecule.editor", messageSource)));
    }

}
