import React from 'react';
import { BrowserRouter as Router, Route } from "react-router-dom";
import Register from './Components/Register';
import Login from './Components/Login';
import Navbar from './Components/Navbar';
import Newsfeed from './Components/Newsfeed';
import Newspage from './Components/Newspage';
import User from './Components/User';
import logo from './logo.svg';
import "bootstrap-css-only/css/bootstrap.min.css";
import PrivateRoute from "./Components/PrivateRoute";
import './App.css';

function App() {
  return (
    <div className="App">
      
      <Router>
          <Navbar />
          <Route exact path="/register" component={Register} />
          <Route exact path="/login" component={Login} />
          <Route exact path="/news" component={Newsfeed} />
          <Route exact path="/" component={Newsfeed} />
          <PrivateRoute
              exact
              path="/news/:handle"
              component={Newspage}
          />
          <PrivateRoute
              exact
              path="/users/:handle"
              component={User}
          />
      </Router>
    </div>
  );
}

export default App;
