// Renders the top navigation bar into #navbar-root, highlighting the active page.
function renderNav(active) {
  const root = document.getElementById("navbar-root");
  if (!root) return;

  const links = [
    { href: "dashboard.html", label: "Dashboard", key: "dashboard" },
    { href: "profile.html", label: "My Profile", key: "profile" },
    { href: "recommendations.html", label: "Recommendations", key: "recommendations" },
    { href: "browse.html", label: "Browse Internships", key: "browse" },
    { href: "applications.html", label: "My Applications", key: "applications" },
  ];

  root.innerHTML = `
    <div class="topbar">
      <div class="brand">
        <span class="dot"></span>
        <div>
          PM Internship Matcher
          <small>AI-based recommendation engine</small>
        </div>
      </div>
      <div class="nav-links">
        ${links.map(l => `<a href="${l.href}" class="${active === l.key ? 'active' : ''}">${l.label}</a>`).join("")}
        <button class="nav-links__logout btn-logout" style="border:1px solid rgba(255,255,255,0.2); color:#fff; padding:8px 14px; border-radius:999px; font-size:13px; cursor:pointer;">Logout</button>
      </div>
    </div>`;

  root.querySelector(".nav-links__logout").addEventListener("click", () => {
    TokenStore.clear();
    window.location.href = "index.html";
  });
}

function requireAuthOrRedirect() {
  if (!TokenStore.isLoggedIn()) {
    window.location.href = "index.html";
  }
}
