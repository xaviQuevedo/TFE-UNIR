import React from "react";
import { BrowserRouter as Router, Route, Routes as ReactRoutes } from "react-router-dom";

import Home from "../views/Home";
import Login from "../views/Login";
import AdminDashboard from "../views/AdminDashboard";
import CreateUser from "../components/admin/RegisterForm";
import UpdateUser from "../components/admin/UserTable";
import AssignPatients from "../components/admin/AssignPatients";
import PhysiotherapistDashboard from "../views/PhysiotherapistDashboard";
import AssignQuestionnaire from "../components/physiotherapist/AssignQuestionnaires";
//import Register from "../views/Register";
//import Questionnaires from "../views/Questionnaires";
//import Users from "../views/Users";

const Routes = () => {
  return (
    <Router>
      <ReactRoutes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/admin/create-user" element={<CreateUser />} />
        <Route path="/admin/update-user" element={<UpdateUser />} />
        <Route path="/admin/assign-patients" element={<AssignPatients />} />
        <Route path="/pyshiotherapist/dashboard" element={<PhysiotherapistDashboard/>} />
        <Route path= "/physiotherapist/assign-questionnaire" element={<AssignQuestionnaire />} />
        {/*<Route path="/register" element={<Register />} />
        <Route path="/questionnaires" element={<Questionnaires />} />
        <Route path="/users" element={<Users />} /> */}
      </ReactRoutes>
    </Router>
  );
};

export default Routes;