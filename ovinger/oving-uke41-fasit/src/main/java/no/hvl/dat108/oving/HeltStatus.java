package no.hvl.dat108.oving;

// Det som skal hentes ut av helten som JSON. Helt har private felt og navn()/hp()
// i stedet for getNavn()/getHp(), saa Jackson finner ingenting aa skrive der.
public record HeltStatus(String navn, int hp, int maxHp, boolean lever) {

    public static HeltStatus fra(Helt helt) {
        return new HeltStatus(helt.navn(), helt.hp(), helt.maxHp(), helt.lever());
    }
}
