package uk.org.squirm3.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Random;

import uk.org.squirm3.model.type.AtomType;
import uk.org.squirm3.model.type.ReactionType;
import uk.org.squirm3.model.type.def.WildcardType;

public final class Reaction {
    private final ReactionType aType, bType;
    private final int aState, bState;

    private final boolean bondedBefore, bondedAfter;
    private final int futureAState, futureBState;
    
    private final double probability;

    public Reaction(final ReactionType aType, final int aState,
            final boolean bondedBefore, final ReactionType bType,
            final int bState, final int futureAState,
            final boolean bondedAfter, final int futureBState,
            final double probability) {
        this.aType = checkNotNull(aType);
        this.aState = aState;
        this.bondedBefore = bondedBefore;
        this.bType = checkNotNull(bType);
        this.bState = bState;
        this.futureAState = futureAState;
        this.bondedAfter = bondedAfter;
        this.futureBState = futureBState;
        this.probability = probability;
    }

    public Reaction(final ReactionType aType, final int aState,
            final boolean bondedBefore, final ReactionType bType,
            final int bState, final int futureAState,
            final boolean bondedAfter, final int futureBState,
            final int probability) {
        this.aType = checkNotNull(aType);
        this.aState = aState;
        this.bondedBefore = bondedBefore;
        this.bType = checkNotNull(bType);
        this.bState = bState;
        this.futureAState = futureAState;
        this.bondedAfter = bondedAfter;
        this.futureBState = futureBState;
        this.probability = probability;
    }

	@Override
    public String toString() {
	    return _toString().concat(" (p=" + probability + ")");
    }
	
	public String toStringForButton() {
	    return _toString().concat("    ");
	}
	
	private String _toString() {
        final StringBuffer stringBuffer = new StringBuffer(15);
        stringBuffer.append(aType.getCharacterIdentifier()).append(aState);
        if (!bondedBefore) {
            stringBuffer.append(" + ");
        }
        stringBuffer.append(bType.getCharacterIdentifier()).append(bState);
        stringBuffer.append(" => ");
        stringBuffer.append(aType.getCharacterIdentifier())
                .append(futureAState);
        if (!bondedAfter) {
            stringBuffer.append(" + ");
        }
        stringBuffer.append(bType.getCharacterIdentifier())
                .append(futureBState);
        return stringBuffer.toString();
	}

    public boolean tryOn(final Atom a, final Atom b) {
        if (rejectReactionOn(a, b)) {
            return false;
        }
        applyReactionOn(a, b);
        return true;
    }

    private boolean rejectReactionOn(final Atom a, final Atom b) {
        // is the type for 'a' specified and correct?
        if (aType instanceof AtomType && a.getType() != aType) {
            return true;
        }
        // is the type for 'b' specified and correct?
        if (bType instanceof AtomType && b.getType() != bType) {
            return true;
        }
        // is the type for 'b' specified as matching that of 'a' and correct?
        if (bType instanceof WildcardType && bType == aType
                && b.getType() != a.getType()) {
            return true; // both x or both y
        }
        // is the state of 'a' and 'b' correct?
        if (aState != a.getState() || bState != b.getState()) {
            return true;
        }
        // is the bonded/not status correct?
        if (bondedBefore && !a.hasBondWith(b) || !bondedBefore
                && a.hasBondWith(b)) {
            return true;
        }
        // after all those checks, now check probability
        if (probability >= 1) {
        	return false;
        } else if (new Random().nextDouble() < probability) {
        	return false;
        } else {
        	return true;
        }
    }

    private void applyReactionOn(final Atom a, final Atom b) {
        if (!bondedBefore && bondedAfter) {
            a.bondWith(b);
        } else if (bondedBefore && !bondedAfter) {
            a.breakBondWith(b);
        }
        a.setState(futureAState);
        b.setState(futureBState);
    }
}
