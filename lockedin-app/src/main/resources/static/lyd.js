// Lyd for LockedIn.
//
// Effektene spilles fra lydfiler i static/lyd/ (se LESMEG.md der). Mangler
// en fil, lager Web Audio API en enkel tone i stedet, så spillet virker
// uten filer. Nettleseren nekter lyd før brukeren har klikket noe. Derfor
// starter alt først i lyd.slaaPaa(), som kalles fra en knapp.

const lyd = (() => {
    // Effektene spillet bruker, og filene de leses fra. Ta bort // foran
    // en linje når filen ligger i static/lyd/. Uten fil: en enkel tone.
    const FILER = {
        // slag:  "lyd/slag.ogg",   // spilleren slår
        // treff: "lyd/treff.ogg",  // monsteret blir truffet
        // drept: "lyd/drept.ogg",  // monsteret dør
        // loot:  "lyd/loot.ogg",   // en skatt plukkes opp
        // feil:  "lyd/feil.ogg"    // noe gikk galt
    };

    let ctx = null;                 // AudioContext, lages først ved slaaPaa
    let musikkTimer = null;
    let paa = false;
    const lastet = {};              // navn -> Audio, bare filer som finnes

    function lastFiler() {
        for (const [navn, fil] of Object.entries(FILER)) {
            const a = new Audio();
            a.preload = "auto";
            a.addEventListener("canplaythrough", () => { lastet[navn] = a; },
                { once: true });
            a.src = fil;
        }
    }

    function tone(hz, varighet, type = "square", styrke = 0.08, start = 0) {
        if (!ctx) return;
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        osc.type = type;
        osc.frequency.value = hz;
        gain.gain.setValueAtTime(styrke, ctx.currentTime + start);
        gain.gain.exponentialRampToValueAtTime(0.001,
            ctx.currentTime + start + varighet);
        osc.connect(gain).connect(ctx.destination);
        osc.start(ctx.currentTime + start);
        osc.stop(ctx.currentTime + start + varighet);
    }

    // Reserve når filen mangler.
    const toner = {
        slag:  () => tone(880, 0.06, "square", 0.06),
        treff: () => { tone(220, 0.12, "sawtooth", 0.1);
                       tone(180, 0.15, "sawtooth", 0.1, 0.06); },
        drept: () => [660, 550, 440, 330].forEach((hz, i) =>
                       tone(hz, 0.15, "square", 0.07, i * 0.1)),
        loot:  () => [523, 659, 784, 1047].forEach((hz, i) =>
                       tone(hz, 0.1, "triangle", 0.08, i * 0.07)),
        feil:  () => tone(110, 0.3, "sawtooth", 0.08)
    };

    // En liten løkke i moll: fire takter bass, en melodilinje over.
    const bass = [110, 110, 130.81, 98];
    const melodi = [220, 261.63, 329.63, 261.63, 220, 196, 220, 0];
    let takt = 0;

    function spillTakt() {
        tone(bass[takt % bass.length], 0.9, "triangle", 0.04);
        melodi.forEach((hz, i) => {
            if (hz) tone(hz, 0.22, "square", 0.015, i * 0.125);
        });
        takt = takt + 1;
    }

    return {
        slaaPaa() {
            if (!ctx) {
                ctx = new (window.AudioContext || window.webkitAudioContext)();
                lastFiler();
            }
            if (ctx.state === "suspended") ctx.resume();
            paa = true;
            if (!musikkTimer) {
                spillTakt();
                musikkTimer = setInterval(spillTakt, 1000);
            }
        },
        slaaAv() {
            paa = false;
            clearInterval(musikkTimer);
            musikkTimer = null;
        },
        erPaa() { return paa; },

        // lyd.spill("treff"): filen hvis den finnes, ellers en tone.
        spill(navn) {
            if (!paa) return;
            const a = lastet[navn];
            if (a) {
                const kopi = a.cloneNode();     // flere samtidig går fint
                kopi.volume = 0.6;
                kopi.play();
            } else if (toner[navn]) {
                toner[navn]();
            }
        }
    };
})();
