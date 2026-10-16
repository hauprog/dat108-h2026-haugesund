// Myntregn: et lite eksempelspill. Bytt det ut med ditt eget.
//
// Mønsteret er det samme som i Spill-klassen i F14: tilstand er et objekt
// som inneholder alt som endrer seg, og tegn() oppdaterer siden ut fra det.
//
// Spillet viser ett eksempel på hver av de fire tingene som anbefales å få
// med (README, «Fire ting å få med»). De er merket «Punkt 1» til «Punkt 4»:
// Punkt 1: alt som vises tegnes fra tilstand av tegn().
// Punkt 2: array-metoder på tilstand.mynter (forEach, filter, find, map).
// Punkt 3: tick() kjøres av setInterval, også når spilleren ikke gjør noe.
// Punkt 4: når spillet er over, viser tegn() skjemaet med poengene.
//          Skjemaet sender dem til serveren (RekordController).

const SPILLTID = 30_000; // ms
const LEVETID = 1500;    // ms en mynt blir liggende
const TICK = 100;        // ms mellom hver tick

let tilstand = nyTilstand();
let tickId = null;

function nyTilstand() {
    return {
        poeng: 0,
        igjen: SPILLTID,
        mynter: [],
        nesteId: 1,
        startet: false,
        ferdig: false,
    };
}

function nyMynt() {
    return {
        id: tilstand.nesteId++,
        x: Math.random() * 90, // prosent av bredden
        y: Math.random() * 85, // prosent av høyden
        igjen: LEVETID,
    };
}

// ---------- Tid (punkt 3) ----------

function start() {
    tilstand = nyTilstand();
    tilstand.startet = true;
    clearInterval(tickId); // aldri to tick samtidig, ellers går spillet dobbelt så fort
    tickId = setInterval(tick, TICK);
    tegn();
}

function tick() {
    tilstand.igjen -= TICK;

    // Punkt 2: alle myntene eldes, og når de er for gamle forsvinner de
    tilstand.mynter.forEach(m => m.igjen -= TICK);
    tilstand.mynter = tilstand.mynter.filter(m => m.igjen > 0);

    if (Math.random() < 0.25) {
        tilstand.mynter.push(nyMynt());
    }
    if (tilstand.igjen <= 0) {
        slutt();
    }
    tegn();
}

function slutt() {
    clearInterval(tickId);
    tilstand.ferdig = true;
    tilstand.mynter = [];
}

// ---------- Spilleren ----------

// bruk pointerdown, ikke click: et click telles bare når knappen trykkes ned og
// slippes på samme element. tegn() lager nye mynt-elementer hvert tick
// (100 ms), så mellom trykk og slipp kan mynten ha blitt byttet ut, og klikket
// blir borte. pointerdown skjer med en gang, for både mus, touch og penn.
document.querySelector("#spill").addEventListener("pointerdown", h => {
    const id = Number(h.target.dataset.id);
    const mynt = tilstand.mynter.find(m => m.id === id);
    if (!mynt || tilstand.ferdig) {
        return;
    }
    tilstand.poeng++;
    tilstand.mynter = tilstand.mynter.filter(m => m !== mynt);
    tegn();
});

document.querySelector("#start").addEventListener("click", start);

// ---------- Tegning (punkt 1) ----------

function tegn() {
    const sekunder = Math.ceil(tilstand.igjen / 1000);
    document.querySelector("#status").textContent =
        `Poeng: ${tilstand.poeng} | Tid: ${sekunder}`;

    const spill = document.querySelector("#spill");
    spill.replaceChildren(...tilstand.mynter.map(lagMynt));

    document.querySelector("#start").hidden = tilstand.startet && !tilstand.ferdig;

    // Punkt 4: skjemaet vises når spillet er over, med poengene i det skjulte feltet
    document.querySelector("#ferdig").hidden = !tilstand.ferdig;
    document.querySelector("#resultat").textContent = tilstand.poeng;
    document.querySelector("#poeng").value = tilstand.poeng;
}

function lagMynt(m) {
    const el = document.createElement("div");
    el.className = "mynt";
    el.dataset.id = m.id;
    el.style.left = m.x + "%";
    el.style.top = m.y + "%";
    el.style.opacity = Math.min(1, m.igjen / 500); // blekner det siste halve sekundet
    return el;
}

tegn();
