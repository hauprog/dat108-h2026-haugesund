// LockedIn i nettleseren. Alt skjer i konsollen (F12) inntil videre:
// utfoer("angrip"), utfoer("rapport"), utfoer("status"), utfoer("hjelp").
//
// Tag F11-foer-timen: tilstand fra F10, pluss det vi ikke rakk i F10
// (angrip, rapport, kommandoer, utfoer, rottetimeren) og fasiten paa
// Deres spill fra F10 (taSkade, lever, finnFoerste, status).

console.log("Du er i: Operasjonsstue 7");

// Hele spillets tilstand i ett objekt. Det er Sykehus.standard() uten klasser.
const tilstand = {
    helt: { navn: "Ole", hp: 100, styrke: 5 },
    angripbare: [
        { navn: "Rottekonge", hp: 20 },
        { navn: "Rottedronning", hp: 500 },
        { navn: "Rottebarn", hp: 9999 }
    ],
    skatter: [
        { navn: "gull som brenner", verdi: 250 },
        { navn: "sandaler", verdi: 250 },
        { navn: "ostekake", verdi: 2147483647 },
        { navn: "gulrotkake", verdi: 2147483646 }
    ]
};

// ---- Angripbar, som funksjoner i stedet for et grensesnitt (Deres spill 1) ----

function taSkade(a, mengde) {
    a.hp = Math.max(0, a.hp - mengde);
}

function lever(a) {
    return a.hp > 0;
}

// Rom.finnFoerste(Predicate) fra Java. find gir elementet, eller undefined (Deres spill 2)
function finnFoerste(kriterium) {
    return tilstand.angripbare.find(kriterium);
}

// ---- Beregning: rene funksjoner ----

function beregnSkade(styrke, kast) {
    return styrke * 2 + kast;
}

const kast = () => Math.floor(Math.random() * 6) + 1;   // Supplier uten typen

// ---- Handlinger ----

function angrip() {
    const rotte = finnFoerste(lever);
    if (!rotte) {                                   // undefined er usann
        console.log("Det er ingenting å angripe her.");
        return;
    }
    const skade = beregnSkade(tilstand.helt.styrke, kast());
    taSkade(rotte, skade);
    console.log(`${tilstand.helt.navn} gjør ${skade} skade på ${rotte.navn}.`);
    if (!lever(rotte)) {
        console.log(`${rotte.navn} er drept.`);
    }
}

function rapport() {
    const levende = tilstand.angripbare.filter(lever).map(a => a.navn);
    const doede = tilstand.angripbare.filter(a => !lever(a)).map(a => a.navn);
    const skatter = tilstand.skatter.map(s => s.navn).join(", ");
    console.log(`Lever:   ${levende}`);
    console.log(`Døde:    ${doede}`);
    console.log(`Skatter: ${skatter}`);
}

// De tre mest verdifulle skattene (Deres spill 3). Kopi foer sort, fordi sort endrer arrayet,
// og comparator, fordi sort() uten sorterer som strenger. Begge deler forklares i F12.
function status() {
    const topp3 = [...tilstand.skatter]
        .sort((a, b) => b.verdi - a.verdi)
        .slice(0, 3)
        .map(s => s.navn);
    console.log(`Topp 3 skatter: ${topp3}`);
}

// ---- Kommandotolken fra F03: et objekt der verdiene er funksjoner ----

const kommandoer = {
    angrip: angrip,
    rapport: rapport,
    status: status,
    se: () => console.log(tilstand)
};

function utfoer(ord) {
    const kommando = kommandoer[ord];           // undefined hvis ordet ikke finnes, uten feil
    if (!kommando) {
        console.log("Ugyldig kommando: " + ord);
        return;
    }
    kommando();
}

// ---- Rottene biter hvert tredje sekund. Én tråd. Hvordan? F14. ----

setInterval(() => {
    const rotte = finnFoerste(lever);
    if (rotte) {
        taSkade(tilstand.helt, 3);
        console.log(`${rotte.navn} biter ${tilstand.helt.navn}! (${tilstand.helt.hp} hp igjen)`);
    }
}, 3000);

console.log('Skriv utfoer("angrip") i konsollen.');