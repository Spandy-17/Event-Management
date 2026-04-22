const posters = {
  cultural: {
    "Dance Fest": "images/cultural/dance.jpg",
    "Drama Night": "images/cultural/drama.jpg"
  },

  technical: {
    "Hackathon": "images/technical/hackathon.jpg",
    "Tech Quiz": "images/technical/quiz.jpg"
  },

  "non-technical": {
    "Debate": "images/non-technical/debate.jpg",
    "Quiz Bee": "images/non-technical/quizbee.jpg"
  },

  sports: {
    "Football": "images/sports/football.jpg",
    "Cricket": "images/sports/cricket.jpg"
  },

  arts: {
    "Painting": "images/arts/painting.jpg",
    "Craft Expo": "images/arts/craft.jpg"
  },

  music: {
    "Battle of Bands": "images/music/bands.jpg",
    "Solo Singing": "images/music/solo.jpg"
  },

  workshops: {
    "Web Dev": "images/workshops/webdev.jpg",
    "AI Workshop": "images/workshops/ai.jpg"
  },

  others: {
    "Open Mic": "images/others/openmic.jpg",
    "Fun Games": "images/others/fungames.jpg"
  }
};

// Full detailed event data for ALL themes
const eventData = {
  cultural: {
    "Dance Fest": {
      poster: posters.cultural["Dance Fest"],
      desc: "Traditional and modern dance performances by our college students.",
      date: "2026-01-12",
      time: "6:00 PM",
      venue: "Main Auditorium",
      organizer: "Cultural Club",
      fee: 200
    },
    "Drama Night": {
      poster: posters.cultural["Drama Night"],
      desc: "Stage plays and skits to entertain and inspire.",
      date: "2026-01-14",
      time: "7:00 PM",
      venue: "Open Theatre",
      organizer: "Drama Society",
      fee: 150
    }
  },

  technical: {
    "Hackathon": {
      poster: posters.technical["Hackathon"],
      desc: "24-hour coding challenge with exciting prizes.",
      date: "2026-02-05",
      time: "9:00 AM",
      venue: "Computer Lab",
      organizer: "Tech Club",
      fee: 100
    },
    "Tech Quiz": {
      poster: posters.technical["Tech Quiz"],
      desc: "Test your technical knowledge in an inter-college quiz.",
      date: "2026-02-07",
      time: "4:00 PM",
      venue: "Lecture Hall 3",
      organizer: "Tech Society",
      fee: 50
    }
  },

  "non-technical": {
    "Debate": {
      poster: posters["non-technical"]["Debate"],
      desc: "Express your ideas confidently in front of an audience.",
      date: "2026-03-01",
      time: "2:00 PM",
      venue: "Seminar Hall",
      organizer: "Literary Club",
      fee: 0
    },
    "Quiz Bee": {
      poster: posters["non-technical"]["Quiz Bee"],
      desc: "Fun general knowledge quiz for all students.",
      date: "2026-03-02",
      time: "3:00 PM",
      venue: "Lecture Hall 2",
      organizer: "Quiz Club",
      fee: 0
    }
  },

  sports: {
    "Football": {
      poster: posters.sports["Football"],
      desc: "Inter-college football tournament to showcase your skills.",
      date: "2026-04-10",
      time: "10:00 AM",
      venue: "Sports Ground",
      organizer: "Sports Committee",
      fee: 0
    },
    "Cricket": {
      poster: posters.sports["Cricket"],
      desc: "Knockout league matches with exciting prizes.",
      date: "2026-04-12",
      time: "9:00 AM",
      venue: "Cricket Ground",
      organizer: "Sports Committee",
      fee: 0
    }
  },

  arts: {
    "Painting": {
      poster: posters.arts["Painting"],
      desc: "Canvas art competition for aspiring artists.",
      date: "2026-05-05",
      time: "11:00 AM",
      venue: "Art Room",
      organizer: "Art Club",
      fee: 50
    },
    "Craft Expo": {
      poster: posters.arts["Craft Expo"],
      desc: "Handmade artwork display and competition.",
      date: "2026-05-06",
      time: "12:00 PM",
      venue: "Exhibition Hall",
      organizer: "Art Society",
      fee: 50
    }
  },

  music: {
    "Battle of Bands": {
      poster: posters.music["Battle of Bands"],
      desc: "Band vs band music battle with audience voting.",
      date: "2026-06-10",
      time: "6:00 PM",
      venue: "Main Auditorium",
      organizer: "Music Club",
      fee: 150
    },
    "Solo Singing": {
      poster: posters.music["Solo Singing"],
      desc: "Individual vocal performances judged by experts.",
      date: "2026-06-12",
      time: "7:00 PM",
      venue: "Lecture Hall 1",
      organizer: "Music Society",
      fee: 100
    }
  },

  workshops: {
    "Web Dev": {
      poster: posters.workshops["Web Dev"],
      desc: "Learn frontend & backend basics from experts.",
      date: "2026-07-01",
      time: "10:00 AM",
      venue: "Computer Lab",
      organizer: "Tech Club",
      fee: 200
    },
    "AI Workshop": {
      poster: posters.workshops["AI Workshop"],
      desc: "Introduction to Artificial Intelligence and ML basics.",
      date: "2026-07-02",
      time: "11:00 AM",
      venue: "Computer Lab",
      organizer: "Tech Society",
      fee: 250
    }
  },

  others: {
    "Open Mic": {
      poster: posters.others["Open Mic"],
      desc: "Sing, speak, or perform in a friendly environment.",
      date: "2026-08-05",
      time: "5:00 PM",
      venue: "Auditorium",
      organizer: "Cultural Club",
      fee: 0
    },
    "Fun Games": {
      poster: posters.others["Fun Games"],
      desc: "Relax and enjoy fun games with your friends.",
      date: "2026-08-06",
      time: "4:00 PM",
      venue: "Open Ground",
      organizer: "Event Committee",
      fee: 0
    }
  }
};

// READ URL PARAMETERS
const params = new URLSearchParams(window.location.search);
const category = params.get("category");
const eventName = params.get("event");

// SAFE LOGIC: fallback if some data missing
const event =
  eventData[category] && eventData[category][eventName]
    ? eventData[category][eventName]
    : {
        poster: posters[category][eventName],
        desc: "Exciting event you shouldn’t miss.",
        date: "To be announced",
        time: "To be announced",
        venue: "College Campus",
        organizer: "Event Committee",
        fee: "Free"
      };

// POPULATE THE PAGE
document.getElementById("eventPoster").src = event.poster;
document.getElementById("eventName").textContent = eventName;
document.getElementById("eventDesc").textContent = event.desc;
document.getElementById("eventDate").textContent = event.date;
document.getElementById("eventTime").textContent = event.time;
document.getElementById("eventVenue").textContent = event.venue;
document.getElementById("eventOrganizer").textContent = event.organizer;
document.getElementById("eventFee").textContent = event.fee;