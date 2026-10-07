// Lyd for LockedIn, uten lydfiler: Web Audio API lager tonene selv.
// Nettleseren nekter lyd før brukeren har klikket noe. Derfor starter alt
// foerst i lyd.slaaPaa(), som kalles fra en knapp.
//
// For "ekte" lyder: legg .wav eller .mp3 i lyd/, og bytt ut
// lyd.bitt/lyd.angrep med  new Audio("lyd/bitt.wav").play()  Musikken kan
// være  const musikk = new Audio("lyd/musikk.mp3"); musikk.loop = true;

const lyd = (() => {
    let ctx = null;            // AudioContext, lages foerst ved slaaPaa
    let musikkTimer = null;
    let paa = false;

    function tone(hz, varighet, type = "square", styrke = 0.08, start = 0) {
        if (!ctx) return;
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        osc.type = type;
        osc.frequency.value = hz;
        gain.gain.setValueAtTime(styrke, ctx.currentTime + start);
        gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + start + varighet);
        osc.connect(gain).connect(ctx.destination);
        osc.start(ctx.currentTime + start);
        osc.stop(ctx.currentTime + start + varighet);
    }

    // En liten løkke i moll: fire takter bass, en melodilinje over.
    const bass = [110, 110, 130.81, 98];                        // A2 A2 C3 G2
    const melodi = [220, 261.63, 329.63, 261.63, 220, 196, 220, 0]; // A3 C4 E4 C4 A3 G3 A3 pause
    let takt = 0;

    function spillTakt() {
        tone(bass[takt % bass.length], 0.9, "triangle", 0.06);
        melodi.forEach((hz, i) => { if (hz) tone(hz, 0.22, "square", 0.025, i * 0.125); });
        takt = takt + 1;
    }

    return {
        slaaPaa() {
            if (!ctx) ctx = new (window.AudioContext || window.webkitAudioContext)();
            if (ctx.state === "suspended") ctx.resume();
            paa = true;
            if (!musikkTimer) { spillTakt(); musikkTimer = setInterval(spillTakt, 1000); }
        },
        slaaAv() {
            paa = false;
            clearInterval(musikkTimer);
            musikkTimer = null;
        },
        erPaa() { return paa; },
        bitt()   { if (paa) { tone(220, 0.12, "sawtooth", 0.1); tone(180, 0.15, "sawtooth", 0.1, 0.06); } },
        angrep() { if (paa) { tone(880, 0.08, "square", 0.08); tone(1320, 0.1, "square", 0.06, 0.05); } },
        drept()  { if (paa) { [660, 550, 440, 330].forEach((hz, i) => tone(hz, 0.15, "square", 0.07, i * 0.1)); } },
        slutt()  { if (paa) { [330, 262, 220, 165].forEach((hz, i) => tone(hz, 0.5, "triangle", 0.1, i * 0.4)); this.slaaAv(); } }
    };
})();