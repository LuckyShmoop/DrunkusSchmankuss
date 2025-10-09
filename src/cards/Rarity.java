package cards;

public enum Rarity {

    // Die vier Seltenheitsstufen mit einem optionalen Wert (Multiplikator)
    COMMON(1.0, "§7"),      // Normaler Effekt (z.B. Graue Farbe)
    UNCOMMON(1.5, "§a"),    // Leicht verbesserter Effekt (z.B. Grüne Farbe)
    RARE(2.0, "§b"),        // Starker Effekt (z.B. Blaue Farbe)
    SUPER_RARE(3.0, "§6");  // Sehr starker Effekt (z.B. Goldene Farbe)

    private final double multiplier;
    private final String colorCode; // Hilfreich für zukünftige UI-Ausgaben

    Rarity(double multiplier, String colorCode) {
        this.multiplier = multiplier;
        this.colorCode = colorCode;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getColorCode() {
        return colorCode;
    }
}