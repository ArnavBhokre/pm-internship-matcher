requireAuthOrRedirect();
renderNav("profile");

let skills = [];      // [{ skillName, proficiencyLevel }]
let interests = [];   // [string]
let locations = [];   // [string]

const alertBox = document.getElementById("alertBox");
function showAlert(msg, type = "error") {
  alertBox.className = "alert alert-" + type + " show";
  alertBox.textContent = msg;
  window.scrollTo({ top: 0, behavior: "smooth" });
}

// ---------- Skills ----------
function renderSkills() {
  const list = document.getElementById("skillsList");
  list.innerHTML = skills.map((s, i) => `
    <div class="skill-row">
      <div style="padding:10px 12px; background:#f7f9fc; border-radius:10px; font-size:14px;">${escapeHtml(s.skillName)}</div>
      <div style="padding:10px 12px; background:#f7f9fc; border-radius:10px; font-size:13px; color:var(--text-muted);">${s.proficiencyLevel}</div>
      <button type="button" class="btn btn-outline btn-sm" onclick="removeSkill(${i})" style="color:var(--danger);">✕</button>
    </div>`).join("");
}
function removeSkill(i) { skills.splice(i, 1); renderSkills(); }

document.getElementById("addSkillBtn").addEventListener("click", () => {
  const nameInput = document.getElementById("newSkillName");
  const levelSelect = document.getElementById("newSkillLevel");
  const name = nameInput.value.trim();
  if (!name) return;
  if (skills.some(s => s.skillName.toLowerCase() === name.toLowerCase())) {
    nameInput.value = ""; return;
  }
  skills.push({ skillName: name, proficiencyLevel: levelSelect.value });
  nameInput.value = "";
  renderSkills();
});

// ---------- Chip inputs (interests / locations) ----------
function setupChipInput(wrapId, inputId, arrayRef, chipClass) {
  const wrap = document.getElementById(wrapId);
  const input = document.getElementById(inputId);

  function render() {
    wrap.querySelectorAll(".chip").forEach(c => c.remove());
    arrayRef.slice().reverse().forEach((val) => {
      const trueIndex = arrayRef.indexOf(val);
      const chip = document.createElement("span");
      chip.className = "chip " + (chipClass || "");
      chip.innerHTML = `${escapeHtml(val)} <span class="remove">&times;</span>`;
      chip.querySelector(".remove").addEventListener("click", () => {
        arrayRef.splice(arrayRef.indexOf(val), 1);
        render();
      });
      wrap.insertBefore(chip, input);
    });
  }

  input.addEventListener("keydown", (e) => {
    if (e.key === "Enter" || e.key === ",") {
      e.preventDefault();
      const val = input.value.trim().replace(/,$/, "");
      if (val && !arrayRef.includes(val)) {
        arrayRef.push(val);
        render();
      }
      input.value = "";
    }
  });

  return render;
}

const renderInterests = setupChipInput("interestsWrap", "interestInput", interests, "saffron");
const renderLocations = setupChipInput("locationsWrap", "locationInput", locations);

function escapeHtml(str) {
  const div = document.createElement("div");
  div.textContent = str;
  return div.innerHTML;
}

// ---------- Load existing profile ----------
async function loadProfile() {
  try {
    const res = await Api.getMyProfile();
    const p = res.data;

    document.getElementById("fullName").value = p.fullName || "";
    document.getElementById("phone").value = p.phone || "";
    document.getElementById("qualification").value = p.qualification || "";
    document.getElementById("fieldOfStudy").value = p.fieldOfStudy || "";
    document.getElementById("collegeOrUniversity").value = p.collegeOrUniversity || "";
    document.getElementById("graduationYear").value = p.graduationYear || "";
    document.getElementById("cgpaOrPercentage").value = p.cgpaOrPercentage || "";
    document.getElementById("careerGoals").value = p.careerGoals || "";
    document.getElementById("bio").value = p.bio || "";
    document.getElementById("preferredWorkMode").value = p.preferredWorkMode || "ANY";

    skills = (p.skills || []).map(s => ({ skillName: s.skill.name, proficiencyLevel: s.proficiencyLevel }));
    renderSkills();

    interests.splice(0, interests.length, ...(p.interests || []));
    renderInterests();

    locations.splice(0, locations.length, ...(p.preferredLocations || []));
    renderLocations();
  } catch (err) {
    console.error(err);
  }
}
loadProfile();

// ---------- Save ----------
document.getElementById("profileForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const saveBtn = document.getElementById("saveBtn");
  saveBtn.disabled = true;
  saveBtn.textContent = "Saving...";

  const payload = {
    fullName: document.getElementById("fullName").value.trim(),
    phone: document.getElementById("phone").value.trim(),
    qualification: document.getElementById("qualification").value,
    fieldOfStudy: document.getElementById("fieldOfStudy").value.trim(),
    collegeOrUniversity: document.getElementById("collegeOrUniversity").value.trim(),
    graduationYear: document.getElementById("graduationYear").value ? Number(document.getElementById("graduationYear").value) : null,
    cgpaOrPercentage: document.getElementById("cgpaOrPercentage").value ? Number(document.getElementById("cgpaOrPercentage").value) : null,
    careerGoals: document.getElementById("careerGoals").value.trim(),
    bio: document.getElementById("bio").value.trim(),
    interests,
    preferredLocations: locations,
    preferredWorkMode: document.getElementById("preferredWorkMode").value,
    skills
  };

  try {
    await Api.updateProfile(payload);
    showAlert("Profile saved! Redirecting to your recommendations...", "success");
    setTimeout(() => window.location.href = "recommendations.html", 1200);
  } catch (err) {
    showAlert(err.message);
  } finally {
    saveBtn.disabled = false;
    saveBtn.textContent = "Save Profile";
  }
});
