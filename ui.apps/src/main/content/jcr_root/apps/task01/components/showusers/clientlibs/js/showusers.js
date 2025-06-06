$(document).ready(function () {
  console.log("JS Loaded ✅");

  $.ajax({
    url: '/bin/showUsers.json',
    method: 'GET',
    dataType: 'json',

    success: function (users) {
      const list = $('#users-list');
      if (list.length === 0) return;

      list.empty(); // ✅ Clear any old items

      users.forEach(function (user) {
        if (!user.name || !user.email || !user.gender) return; // ✅ Skip bad data
        const listItem = `<li>${user.name} (email: ${user.email}) ${user.gender}</li>`;
        list.append(listItem);
      });
    },

    error: function (xhr, status, error) {
      console.error("Error loading users:", error);
      $('#users-container').text("Failed to load users.");
    }
  });
});
