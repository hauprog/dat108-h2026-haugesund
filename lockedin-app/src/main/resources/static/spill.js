"use strict";

// LockedIn i nettleseren: arenaen.
// Samme mønster som i modul 3: ett tilstand-objekt med alt som endrer seg,
// og tegn() som oppdaterer siden ut fra det. Lyd og effekter (juice) er
// ikke tilstand, og ligger utenfor tegn().
//
// Status: hentDndMonster() bruker fetch mot D&D-API-et. hentRom() og
// nyttMonster() bruker LOKALE data. Planen videre:
//   F22: hentRom() med GET og nyttMonster() med POST mot vår server.
//   F23: angrip() sender POST til serveren, som regner ut skaden.

const ROMNR = 13;                               // Likhuset

// Halloween: mørkere farger fra 29. oktober til 1. november, eller med
// ?tema=halloween i adressen. Bare utseende, ingen tilstand.
const idag = new Date();
const erHalloween = (idag.getMonth() === 9 && idag.getDate() >= 29)
    || (idag.getMonth() === 10 && idag.getDate() === 1);
if (erHalloween
        || new URLSearchParams(location.search).get("tema") === "halloween") {
    document.body.classList.add("halloween");
}

// ---- Tilstand ----

const tilstand = {
    rom: null,          // { nr, navn, monstre: [...], skatter: [...] }
    logg: []            // de siste hendelsene, nyeste først
};

// ---- API-laget: alt som skal snakke med serveren, samlet på ett sted ----
//
// Funksjonene er async, så de gir et Promise, akkurat som fetch.
// Da kan resten av koden være lik når vi bytter til serveren.

// Lokale data, med samme form som JSON-en fra GET /api/rom/13.
const lokaltRom = {
    nr: 13,
    navn: "Likhuset (lokale data)",
    monstre: [
        { navn: "Gresskarhodet", hp: 31, maxHP: 31, lever: true },
        { navn: "Spøkelset", hp: 13, maxHP: 13, lever: true },
        { navn: "Vampyren", hp: 66, maxHP: 66, lever: true }
    ],
    skatter: [
        { navn: "godteri", verdi: 31 }
    ]
};

const DND = "https://www.dnd5eapi.co";

// Vandøde i D&D-API-et (index i /api/2014/monsters), til Likhuset.
const VANDOEDE = ["zombie", "skeleton", "ghost", "ghoul", "ghast",
    "mummy", "wraith", "specter", "shadow", "wight"];

// fetch, sjekk statuskoden, og gjør JSON-en om til objekter.
async function hentJson(url) {
    const svar = await fetch(url);
    if (!svar.ok) {
        throw new Error(`${svar.status} fra ${url}`);
    }
    return svar.json();
}

function kastTerning(sider) {
    return Math.floor(Math.random() * sider) + 1;
}

const api = {
    // F22: GET /api/rom/{nr}
    async hentRom(nr) {
        const svar = await fetch(`/api/rom/${nr}`);
        if(!svar.ok){
            throw new Error(`${svar.status} fra serveren.`)
        }
        return svar.json();
    },

    // Et tilfeldig vandødt monster fra D&D-API-et (www.dnd5eapi.co),
    // gjort om til vår form: { navn, hp, maxHP, lever }
    async hentDndMonster() {
        const i = Math.floor(Math.random() * VANDOEDE.length);
        const d = await hentJson(`${DND}/api/2014/monsters/${VANDOEDE[i]}`);
        return { navn: d.name, hp: d.hit_points, maxHP: d.hit_points,
                 lever: true };
    },

    // F22: POST /api/rom/{nr}/monstre, med { navn, hp } som JSON.
    // Svar: 201 Created, og monsteret slik serveren lagret det.
    // NB: nå sendes monsteret ingen steder. Det finnes bare i denne fanen.
    async nyttMonster(nr, m) {
        return m;
    },

    // F23: POST /api/rom/{nr}/angrep, med navnet på monsteret.
    // Svar: { navn, skade, hp, lever }
    // NB: her regner klienten ut skaden selv. Det er det som skal bort.
    async angrip(nr, navn) {
        const m = tilstand.rom.monstre.find(m => m.navn === navn);
        const skade = 5 * 2 + kastTerning(6);   // Helt.beregnSkade
        const hp = Math.max(0, m.hp - skade);
        return { navn, skade, hp, lever: hp > 0 };
    }
};

// ---- Bilder ----
//
// Legg bildefiler i static/bilder/ (se LESMEG.md der), og koble navn til
// fil her. Mangler et bilde, vises forbokstaven i en sirkel i stedet.

const BILDER = {
    // "Rottekonge": "bilder/rotte.png",
    // "Skrujern": "bilder/skrujern.png",
};
const STANDARDBILDE = null;                     // f.eks. "bilder/monster.png"

// ---- Tegning ----

const romnavnEl = document.getElementById("romnavn");
const arenaEl = document.getElementById("arena");
const skatterEl = document.getElementById("skatter");
const loggEl = document.getElementById("logg");
const feilEl = document.getElementById("feil");

// Ett kort per monster. Kortene lages én gang og oppdateres etterpå, så
// effektene ikke avbrytes når siden tegnes på nytt.
const kort = new Map();                         // navn -> element

function lagKort(m) {
    const el = document.createElement("button");
    el.type = "button";
    el.className = "monster";
    el.dataset.navn = m.navn;

    const figur = document.createElement("span");
    figur.className = "figur";
    const fil = BILDER[m.navn] ?? STANDARDBILDE;
    if (fil) {
        const bilde = document.createElement("img");
        bilde.src = fil;
        bilde.alt = "";
        bilde.addEventListener("error", () => figur.replaceChildren(
            lagMerke(m.navn)));
        figur.append(bilde);
    } else {
        figur.append(lagMerke(m.navn));
    }

    const navn = document.createElement("span");
    navn.className = "navn";
    navn.textContent = m.navn;                  // textContent: aldri HTML

    const hp = document.createElement("span");
    hp.className = "hp";
    hp.append(document.createElement("span"));

    const hptekst = document.createElement("span");
    hptekst.className = "hptekst";

    el.append(figur, navn, hp, hptekst);
    el.addEventListener("click", () => angrip(m.navn));
    return el;
}

function lagMerke(navn) {
    const merke = document.createElement("span");
    merke.className = "merke";
    merke.textContent = navn.charAt(0).toUpperCase();
    return merke;
}

function tegn() {
    const rom = tilstand.rom;
    romnavnEl.textContent = rom.navn;

    // Monstrene: lag kort for nye, oppdater alle, fjern de som er borte.
    const navnene = new Set(rom.monstre.map(m => m.navn));
    for (const [navn, el] of kort) {
        if (!navnene.has(navn)) {
            el.remove();
            kort.delete(navn);
        }
    }
    for (const m of rom.monstre) {
        if (!kort.has(m.navn)) {
            const el = lagKort(m);
            kort.set(m.navn, el);
            arenaEl.append(el);
        }
        const el = kort.get(m.navn);
        el.classList.toggle("drept", !m.lever);
        el.disabled = !m.lever;
        el.setAttribute("aria-label", `${m.navn}, ${m.hp} av ${m.maxHP} hp`);
        el.querySelector(".hp > span").style.width =
            (100 * m.hp / m.maxHP) + "%";
        el.querySelector(".hptekst").textContent = m.lever
            ? `${m.hp} / ${m.maxHP} hp`
            : "drept";
    }

    skatterEl.replaceChildren(...rom.skatter.map(s => {
        const li = document.createElement("li");
        const navn = document.createElement("span");
        navn.textContent = s.navn;
        const verdi = document.createElement("span");
        verdi.className = "verdi";
        verdi.textContent = s.verdi;
        li.append(navn, verdi);
        return li;
    }));

    loggEl.replaceChildren(...tilstand.logg.map(tekst => {
        const li = document.createElement("li");
        li.textContent = tekst;
        return li;
    }));
}

function logg(tekst) {
    tilstand.logg = [tekst, ...tilstand.logg].slice(0, 8);
}

function visFeil(tekst) {
    feilEl.textContent = tekst;
    feilEl.hidden = !tekst;
}

// ---- Juice: effekter som ikke er tilstand ----

// Legg på en klasse, og ta den av når animasjonen er ferdig.
function effekt(el, klasse, ms) {
    el.classList.remove(klasse);
    void el.offsetWidth;                        // start animasjonen på nytt
    el.classList.add(klasse);
    setTimeout(() => el.classList.remove(klasse), ms);
}

// Et tall som flyter opp over kortet og forsvinner.
function flytendeTekst(tekst, el) {
    const t = document.createElement("div");
    t.className = "flyt";
    t.textContent = tekst;
    t.style.left = (el.offsetLeft + el.offsetWidth / 2 - 15) + "px";
    t.style.top = (el.offsetTop + 10) + "px";
    arenaEl.append(t);
    setTimeout(() => t.remove(), 800);
}

// ---- Handlinger ----

async function angrip(navn) {
    const m = tilstand.rom.monstre.find(m => m.navn === navn);
    if (!m || !m.lever) {
        return;
    }
    lyd.spill("slag");
    try {
        const svar = await api.angrip(tilstand.rom.nr, navn);
        m.hp = svar.hp;
        m.lever = svar.lever;
        logg(`Du gjør ${svar.skade} skade på ${navn}.`);
        if (!svar.lever) {
            logg(`${navn} er drept.`);
        }
        tegn();

        const el = kort.get(navn);
        flytendeTekst("-" + svar.skade, el);
        effekt(el, svar.lever ? "rist" : "blink", 300);
        lyd.spill(svar.lever ? "treff" : "drept");
    } catch (feil) {
        lyd.spill("feil");
        visFeil("Angrepet feilet: " + feil.message);
    }
}

// Et monster fra D&D-API-et inn i rommet, via api.nyttMonster().
async function innkall() {
    try {
        const m = await api.hentDndMonster();
        if (tilstand.rom.monstre.some(a => a.navn === m.navn)) {
            logg(`${m.navn} er her allerede.`);
        } else {
            const ny = await api.nyttMonster(tilstand.rom.nr, m);
            tilstand.rom.monstre.push(ny);
            logg(`${ny.navn} dukker opp! (${ny.hp} hp)`);
        }
        tegn();
        effekt(kort.get(m.navn), "blink", 300);
        lyd.spill("loot");
    } catch (feil) {
        lyd.spill("feil");
        visFeil("Fikk ikke innkalt et monster: " + feil.message);
    }
}

document.getElementById("innkall").addEventListener("click", innkall);
document.getElementById("bygg").href = `/rom/${ROMNR}`;

// ---- Lydknappen ----

const lydknapp = document.getElementById("lydknapp");
lydknapp.addEventListener("click", () => {
    if (lyd.erPaa()) {
        lyd.slaaAv();
    } else {
        lyd.slaaPaa();
    }
    lydknapp.textContent = lyd.erPaa() ? "Lyd på" : "Lyd av";
    lydknapp.setAttribute("aria-pressed", lyd.erPaa());
});

// ---- Start ----

// Henter rommet og tegner det på nytt.
async function oppdater() {
    try {
        tilstand.rom = await api.hentRom(ROMNR);
        visFeil("");
        tegn();
    } catch (feil) {
        visFeil("Fikk ikke hentet rommet: " + feil.message);
    }
}

async function start() {
    await oppdater();
    if (tilstand.rom) {
        logg(`Du er i ${tilstand.rom.navn}.`);
        tegn();
    }
}

start();
setInterval(oppdater, 2000);