package client;

import constants.GameConstants;

public enum MapleStat {

    SKIN(0x1), // byte
    FACE(0x2), // int
    HAIR(0x4), // int
    LEVEL(0x10), // byte
    JOB(0x20), // short
    STR(0x40), // short
    DEX(0x80), // short
    INT(0x100), // short
    LUK(0x200), // short
    HP(0x400), // int
    MAXHP(0x800), // int
    MP(0x1000), // int
    MAXMP(0x2000), // int
    AVAILABLEAP(0x4000), // short
    AVAILABLESP(0x8000), // short (depends)
    EXP(0x10000), // int
    FAME(0x20000), // int
    MESO(0x40000), // int
    PET(0x180008), // Pets: 0x8 + 0x80000 + 0x100000  [3 longs]
    GACHAPONEXP(0x200000), // int
    FATIGUE(0x400000), // byte
    CHARISMA(0x800000), // ambition int
    INSIGHT(0x1000000),
    WILL(0x2000000), // int
    CRAFT(0x4000000), // dilligence, int
    SENSE(0x8000000), // empathy, int
    CHARM(0x10000000), // int
    TRAIT_LIMIT(0x20000000), // 12 bytes
	BATTLE_EXP(0x40000000), // byte, int, int
	BATTLE_RANK(0x80000000L), // byte
	BATTLE_POINTS(0x100000000L),
	ICE_GAGE(0x200000000L),
	VIRTUE(0x400000000L);

    private final long i;

    private MapleStat(long i) {
        this.i = i;
    }

    public long getValue() {
        return i;
    }

    public static final MapleStat getByValue(final long value) {
        for (final MapleStat stat : MapleStat.values()) {
            if (stat.i == value) {
                return stat;
            }
        }
        return null;
    }

    public static enum Temp {

        STR(0x1),
        DEX(0x2),
        INT(0x4),
        LUK(0x8),
        WATK(0x10),
        WDEF(0x20),
        MATK(0x40),
        MDEF(0x80),
        ACC(0x100),
        AVOID(0x200),
        SPEED(0x400), // byte
        JUMP(0x800), // byte
		UNKNOWN(0x1000); // byte
		
        private final int i;

        private Temp(int i) {
            this.i = i;
        }

        public int getValue() {
            return i;
        }
    }
}
