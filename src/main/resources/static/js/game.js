/* Game Logic for HMK */

// Read configuration from the DOM (Data Attributes)
const configElement = document.getElementById("game-config");
const USER_ID = configElement ? configElement.dataset.userId : null;
const GAME_ID = configElement ? configElement.dataset.gameId : 1;

let targetNumber = 0;
let attempts = 0;
let gameOver = false;

// --- Core Functions ---

function startGame() {
  targetNumber = Math.floor(Math.random() * 100) + 1;
  attempts = 0;
  gameOver = false;

  const input = document.getElementById("guessInput");
  const feedback = document.getElementById("feedback");
  const restartBtn = document.getElementById("restartBtn");

  input.value = "";
  input.disabled = false;
  input.focus();

  feedback.innerText = "¡Empieza a adivinar!";
  feedback.style.color = "#666";
  restartBtn.classList.add("hidden");

  console.log("Debug: Target is " + targetNumber);
}

function makeGuess() {
  if (gameOver) return;

  const input = document.getElementById("guessInput");
  const feedback = document.getElementById("feedback");
  const guess = parseInt(input.value);

  // Validation
  if (isNaN(guess) || guess < 1 || guess > 100) {
    Swal.fire({
      icon: "error",
      title: "Uy...",
      text: "¡Por favor ingresa un número entre 1 y 100!",
      confirmButtonColor: "#ff6b6b",
    });
    return;
  }

  attempts++;

  if (guess === targetNumber) {
    handleVictory(attempts);
  } else if (guess < targetNumber) {
    feedback.innerHTML =
      "¡Muy bajo! 📉 <span style='color:#e67e22'>Intenta más alto.</span>";
    input.value = "";
    input.focus();
  } else {
    feedback.innerHTML =
      "¡Muy alto! 📈 <span style='color:#e67e22'>Intenta más bajo.</span>";
    input.value = "";
    input.focus();
  }
}

function handleVictory(attempts) {
  gameOver = true;
  document.getElementById("guessInput").disabled = true;
  document.getElementById("restartBtn").classList.remove("hidden");
  document.getElementById("feedback").innerText = "";

  // SweetAlert2 Effect
  Swal.fire({
    title: "🎉 ¡VICTORIA!",
    html: `¡Encontraste el número en <b>${attempts}</b> intentos!`,
    imageUrl: "/images/wapo.jpg", // Make sure this image exists
    imageWidth: 150,
    imageAlt: "Victory Image",
    backdrop: `
            rgba(0,0,123,0.4)
            url("/images/confetti.gif") 
            left top
            no-repeat
        `,
    confirmButtonText: "¡Genial!",
    confirmButtonColor: "#007bff",
  });

  submitScore(attempts);
}

// --- API Calls ---

async function submitScore(score) {
  if (!USER_ID) {
    console.error("User ID not found, cannot submit score.");
    return;
  }

  try {
    const response = await fetch("/api/scores", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId: parseInt(USER_ID),
        gameId: parseInt(GAME_ID),
        score: score,
      }),
    });
    if (response.ok) {
      loadLeaderboard();
    }
  } catch (e) {
    console.error("Error submitting score:", e);
  }
}

async function loadLeaderboard() {
  try {
    const response = await fetch(`/api/scores/top?gameId=${GAME_ID}`);
    const scores = await response.json();
    const tbody = document.getElementById("leaderboard-body");

    tbody.innerHTML = ""; // Clear existing

    if (scores.length === 0) {
      tbody.innerHTML =
        '<tr><td colspan="2" style="text-align:center">Aún no hay puntuaciones. ¡Sé el primero!</td></tr>';
      return;
    }

    scores.forEach((s, index) => {
      let rankIcon = "";
      if (index === 0) rankIcon = "🥇 ";
      else if (index === 1) rankIcon = "🥈 ";
      else if (index === 2) rankIcon = "🥉 ";

      const row = `
                <tr>
                    <td>${rankIcon}${s.username}</td>
                    <td class="score-high">${s.score}</td>
                </tr>
            `;
      tbody.innerHTML += row;
    });
  } catch (e) {
    console.error("Error loading leaderboard:", e);
  }
}

// --- Initialization ---

document.addEventListener("DOMContentLoaded", () => {
  // Falling Burgers
  createBurgers();

  // Event Listeners
  const input = document.getElementById("guessInput");
  if (input) {
    input.addEventListener("keypress", function (e) {
      if (e.key === "Enter") makeGuess();
    });

    // Start game initially
    startGame();
    loadLeaderboard();
  }
});

function createBurgers() {
  const container = document.getElementById("burgerRain");
  if (!container) return;

  const burgerCount = 15;
  for (let i = 0; i < burgerCount; i++) {
    const burger = document.createElement("div");
    burger.classList.add("burger");
    burger.innerHTML = "🍔";
    burger.style.left = Math.random() * 100 + "vw";
    burger.style.animationDuration = Math.random() * 3 + 2 + "s";
    burger.style.animationDelay = Math.random() * 5 + "s";
    burger.style.fontSize = Math.random() * 1.5 + 1 + "rem";
    container.appendChild(burger);
  }
}
