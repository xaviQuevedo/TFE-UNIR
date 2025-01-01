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
import PatientDasboard from "../views/PatientDashboard";
import CompleteQuestionnaires from "../components/patient/CompleteQuestionnaires";
import AnswerQuestionnaire from "../components/patient/AnswerQuestionnaire";
import PatientProgress from "../components/patient/PatientProgress";
import CompletedQuestionnaires from "../components/patient/CompletedQuestionnaires";
import QuestionnaireResponses from "../components/patient/QuestionnaireResponses";
import ViewAssignedPatients from "../components/physiotherapist/ViewAssignedPatients";
import Statistics from "../components/physiotherapist/Statistics";
import ClinicalHistory from "../components/physiotherapist/ClinicalHistory";
import ChangePassword from "../components/user/ChangePassword";
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
        <Route path="/physiotherapist/assign-questionnaire" element={<AssignQuestionnaire />} />
        <Route path="/physiotherapist/view-assigned-patients" element={<ViewAssignedPatients />} />
        <Route path="/physiotherapist/statistics" element={<Statistics />} />
        <Route path="/physiotherapist/clinical-history" element={<ClinicalHistory />} />
        <Route path="/patient/change-password" element={<ChangePassword />} />
        <Route path="/physiotherapist/change-password" element={<ChangePassword />} />
        


        <Route path="/patient/dashboard" element={<PatientDasboard />} />
        <Route path="/patient/pending-questionnaires" element={<CompleteQuestionnaires/>} />
        <Route path="/patient/answer-questionnaire/:questionnaireId" element={<AnswerQuestionnaire />} />
        <Route path="/patient/progress" element={<PatientProgress />} />
        <Route path="/patient/completed-questionnaires" element={<CompletedQuestionnaires />} />
        <Route path="/patient/completed-questionnaire/:questionnaireId" element={<QuestionnaireResponses />} />
      </ReactRoutes>
    </Router>
  );
};

export default Routes;