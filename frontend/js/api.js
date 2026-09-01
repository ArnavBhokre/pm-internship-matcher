// ============================================================
// Small fetch wrapper: attaches JWT, parses JSON, standardizes errors.
// ============================================================

const TokenStore = {
  get token() { return localStorage.getItem("pmim_token"); },
  set token(v) { v ? localStorage.setItem("pmim_token", v) : localStorage.removeItem("pmim_token"); },
  get refreshToken() { return localStorage.getItem("pmim_refresh_token"); },
  set refreshToken(v) { v ? localStorage.setItem("pmim_refresh_token", v) : localStorage.removeItem("pmim_refresh_token"); },
  get studentId() { return localStorage.getItem("pmim_student_id"); },
  set studentId(v) { v ? localStorage.setItem("pmim_student_id", v) : localStorage.removeItem("pmim_student_id"); },
  get fullName() { return localStorage.getItem("pmim_full_name"); },
  set fullName(v) { v ? localStorage.setItem("pmim_full_name", v) : localStorage.removeItem("pmim_full_name"); },
  get email() { return localStorage.getItem("pmim_email"); },
  set email(v) { v ? localStorage.setItem("pmim_email", v) : localStorage.removeItem("pmim_email"); },

  saveAuth(authResponse) {
    this.token = authResponse.token;
    this.refreshToken = authResponse.refreshToken;
    this.studentId = authResponse.studentId;
    this.fullName = authResponse.fullName;
    this.email = authResponse.email;
  },
  clear() {
    ["pmim_token", "pmim_refresh_token", "pmim_student_id", "pmim_full_name", "pmim_email"]
      .forEach(k => localStorage.removeItem(k));
  },
  isLoggedIn() { return !!this.token; }
};

async function apiRequest(path, { method = "GET", body = null, auth = true } = {}) {
  const headers = { "Content-Type": "application/json" };
  if (auth && TokenStore.token) {
    headers["Authorization"] = "Bearer " + TokenStore.token;
  }

  let response;
  try {
    response = await fetch(API_BASE_URL + path, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined
    });
  } catch (networkErr) {
    throw new Error("Could not reach the server. Is the backend running on " + API_BASE_URL + "?");
  }

  let payload = null;
  try { payload = await response.json(); } catch (e) { /* no body */ }

  if (!response.ok) {
    const message = (payload && payload.message) ? payload.message : ("Request failed (" + response.status + ")");
    throw new Error(message);
  }
  return payload;
}

const Api = {
  register: (data) => apiRequest("/auth/register", { method: "POST", body: data, auth: false }),
  verifyOtp: (data) => apiRequest("/auth/verify-otp", { method: "POST", body: data, auth: false }),
  resendOtp: (data) => apiRequest("/auth/resend-otp", { method: "POST", body: data, auth: false }),
  login: (data) => apiRequest("/auth/login", { method: "POST", body: data, auth: false }),
  forgotPassword: (data) => apiRequest("/auth/forgot-password", { method: "POST", body: data, auth: false }),
  resetPassword: (email, otpCode, newPassword) =>
    apiRequest(`/auth/reset-password?email=${encodeURIComponent(email)}&otpCode=${encodeURIComponent(otpCode)}&newPassword=${encodeURIComponent(newPassword)}`,
      { method: "POST", auth: false }),

  getMyProfile: () => apiRequest("/students/me"),
  updateProfile: (data) => apiRequest("/students/me/profile", { method: "PUT", body: data }),

  listInternships: (params = {}) => {
    const qs = new URLSearchParams(Object.fromEntries(Object.entries(params).filter(([,v]) => v))).toString();
    return apiRequest("/internships" + (qs ? "?" + qs : ""), { auth: false });
  },
  getInternship: (id) => apiRequest(`/internships/${id}`, { auth: false }),

  getRecommendations: (limit = 20) => apiRequest(`/recommendations?limit=${limit}`),

applyToInternship: (internshipId) => apiRequest(`/applications/${internshipId}`, { method: "POST" }),
myApplications: () => apiRequest("/applications/me"),
withdrawApplication: (applicationId) =>
  apiRequest(`/applications/${applicationId}/withdraw`, { method: "PUT" })
};
