package uk.org.squirm3.model;

import uk.org.squirm3.model.type.AtomType;

public abstract class Atoms {

    public static Atom createAtom(final AtomType type, final int state) {
        return new Atom(FixedPoint.ORIGIN, type, state, 50);
    }

    public static Atom createFixedAtom(final AtomType type, final int state,
            final float x, final float y) {
        return new Atom(new FixedPoint(x, y), type, state, 50);
    }

    public static Atom createMobileAtomWithRandomSpeed(final AtomType type,
            final int state, final float x, final float y) {
        // final float ms = Atom.getAtomSize() / 3;
        // XXX EMRE
        final double angle = Math.random() * 360;
        final double size = Math.random() * 50;
        final float ms = 150 / Float.valueOf(Double.toString(size));
        final float dx = (float) (Math.cos(angle) * ms - ms / 2.0);
        final float dy = (float) (Math.sin(angle) * ms - ms / 2.0);
        return new Atom(new MobilePoint(x, y, dx, dy, 0, 0), type, state, size);
    }

    public static Atom createAtom(final AtomType type, final int state,
            final float x, final float y, final boolean fixed) {
        if (fixed) {
            return createFixedAtom(type, state, x, y);
        }
        return createMobileAtomWithRandomSpeed(type, state, x, y);
    }

}
