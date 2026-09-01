requireAuthOrRedirect();
renderNav("browse");

const content = document.getElementById("content");

function moneyTag(v) {
  return v ? `<span class="tag money">₹${v.toLocaleString("en-IN")}/mo</span>` : "";
}

function renderInternshipCard(item) {
  const deadline = item.applicationDeadline ? new Date(item.applicationDeadline).toLocaleDateString("en-IN", { day: "numeric", month: "short", year: "numeric" }) : "Not specified";
  const skillNames = (item.requiredSkills || []).map(rs => rs.skill.name);

  return `
    <div class="rec-card">
      <div class="rec-top">
        <div>
          <h3 class="rec-title">${item.title}</h3>
          <p class="rec-company">${item.companyName} &middot; ${item.sector || "General"}</p>
          <div class="tag-row">
            <span class="tag location">📍 ${item.location || "Not specified"}</span>
            <span class="tag mode">${item.workMode || "ONSITE"}</span>
            ${moneyTag(item.stipendPerMonth)}
            <span class="tag">⏱ ${item.duration || "-"}</span>
          </div>
        </div>
      </div>
      <p style="font-size:13.5px; color:var(--text-muted); margin:10px 0;">${item.description || ""}</p>
      ${skillNames.length ? `<div class="skills-line"><strong>Skills needed:</strong> ${skillNames.join(", ")}</div>` : ""}
      <p style="font-size:12.5px; color:var(--text-muted); margin:8px 0 0;">Application deadline: ${deadline} &middot; Minimum qualification: ${item.minimumQualification || "Any Graduate"}</p>
      <div class="rec-actions">
        <button class="btn btn-primary btn-sm" onclick="applyTo(${item.id}, this)">Apply Now</button>
      </div>
    </div>`;
}

async function applyTo(internshipId, btn) {
  btn.disabled = true;
  btn.textContent = "Applying...";
  try {
    await Api.applyToInternship(internshipId);
    btn.textContent = "✓ Applied";
    btn.classList.remove("btn-primary");
    btn.classList.add("btn-outline");
  } catch (err) {
    alert(err.message);
    btn.disabled = false;
    btn.textContent = "Apply Now";
  }
}

async function loadInternships(filters = {}) {
  content.innerHTML = `<div class="loader"><div class="spinner"></div> Loading internships...</div>`;
  try {
    const res = await Api.listInternships(filters);
    const items = res.data || [];
    if (!items.length) {
      content.innerHTML = `<div class="empty-state card"><div class="icon">🔎</div><h3>No internships found</h3><p>Try adjusting your search filters.</p></div>`;
      return;
    }
    content.innerHTML = items.map(renderInternshipCard).join("");
  } catch (err) {
    content.innerHTML = `<div class="alert alert-error show">${err.message}</div>`;
  }
}

document.getElementById("searchBtn").addEventListener("click", () => {
  loadInternships({
    keyword: document.getElementById("keywordInput").value.trim(),
    location: document.getElementById("locationInput").value.trim(),
    sector: document.getElementById("sectorInput").value.trim()
  });
});

document.getElementById("clearBtn").addEventListener("click", () => {
  document.getElementById("keywordInput").value = "";
  document.getElementById("locationInput").value = "";
  document.getElementById("sectorInput").value = "";
  loadInternships();
});

loadInternships();
