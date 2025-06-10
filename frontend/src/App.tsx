import React from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import './App.css';
import ChatRoomListPage from "./pages/ChatRoomListPage";

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/chat-room-list" element={<ChatRoomListPage />} />
          <Route path="/" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
        </Routes>
      </Router>
  );
}

export default App
