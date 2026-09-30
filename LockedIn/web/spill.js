"use strict";

// LockedIn i nettleseren.
// Status etter F13.

console.log("Du er i: Operasjonsstue 7");

// ---- Terningen som closure (torsdagsoppgaven del 2) ----

function lagTerning(sider) {
    return () => Math.floor(Math.random() * sider) + 1;
}

// Hele spillets tilstand i ett objekt. Det er Sykehus.standard() uten klasser.
const tilstand = {
    helt: { navn: "Ole", hp: 100, styrke: 5, terning: lagTerning(6) },
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

// ---- Angripbar, som funksjoner i stedet for et grensesnitt ----

function taSkade(a, mengde) {
    a.hp = Math.max(0, a.hp - mengde);
}

function lever(a) {
    return a.hp > 0;
}

// Rom.finnFoerste(Predicate) fra Java. find gir elementet, eller undefined
function finnFoerste(kriterium) {
    return tilstand.angripbare.find(kriterium);
}

// ---- Beregning: ren funksjon ----

function beregnSkade(styrke, kast) {
    return styrke * 2 + kast;
}

// ---- Handlinger: tar (helt, rom), som BiConsumer<Helt, Rom> (del 1) ----

function angrip(helt, rom) {
    const rotte = finnFoerste(lever);
    if (!rotte) {                                   // undefined er usann
        console.log("Det er ingenting å angripe her.");
        return;
    }
    const skade = beregnSkade(helt.styrke, helt.terning());
    taSkade(rotte, skade);
    console.log(`${helt.navn} gjør ${skade} skade på ${rotte.navn}.`);
    if (!lever(rotte)) {
        console.log(`${rotte.navn} er drept.`);
    }
}

function rapport(rom) {
    const levende = rom.angripbare.filter(lever).map(a => a.navn);
    const doede = rom.angripbare.filter(a => !lever(a)).map(a => a.navn);
    const skatter = rom.skatter.map(s => s.navn).join(", ");
    console.log(`Lever:   ${levende}`);
    console.log(`Døde:    ${doede}`);
    console.log(`Skatter: ${skatter}`);
}

function status(rom) {
    const topp3 = [...rom.skatter]                  // kopi: sort endrer paa stedet
        .sort((a, b) => b.verdi - a.verdi)
        .slice(0, 3)
        .map(s => s.navn);
    console.log(`Topp 3 skatter: ${topp3}`);
}

// ---- Kommandotolken: et objekt der verdiene er funksjoner ----

const kommandoer = {
    angrip: (helt, rom) => angrip(helt, rom),
    rapport: (helt, rom) => rapport(rom),
    se: (helt, rom) => console.log(rom),
    status: (helt, rom) => status(rom),
    hjelp: (helt, rom) => console.log("Kommandoer: " + Object.keys(kommandoer).join(", "))
};

function utfoer(ord) {
    const kommando = kommandoer[ord];           // undefined hvis ordet ikke finnes
    if (!kommando) {
        console.log("Ugyldig kommando: " + ord);
        return;
    }
    kommando(tilstand.helt, tilstand);          // rommet er hele tilstanden inntil videre
}

const vaapen = document.getElementById("vaapen");
const siderFelt = vaapen.querySelector("input");
const kastKnapp = vaapen.querySelector("button");
const resultat = vaapen.querySelector("span");

function rullTerning(){
    const sider = Number(siderFelt.value);
    tilstand.helt.terning = lagTerning(sider);
    resultat.textContent = tilstand.helt.terning();
}

kastKnapp.addEventListener("click", rullTerning);


// ---- Rottene biter hvert tredje sekund. Én tråd. Hvordan? F14. ----

setInterval(() => {
    const rotte = finnFoerste(lever);
    if (rotte) {
        taSkade(tilstand.helt, 3);
        console.log(`${rotte.navn} biter ${tilstand.helt.navn}! (${tilstand.helt.hp} hp igjen)`);
    }
}, 3000);

console.log('Skriv utfoer("angrip") i konsollen.');
