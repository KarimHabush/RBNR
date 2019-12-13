import React, { Component } from "react";
import {Grid,Container,Typography,Divider,Button} from '@material-ui/core';
import NewItem from "./NewItem";

const soap = require("soap");



class User extends Component {

	constructor(props) {
	  super(props);
	
	  this.state = {
	  	loading : true,
	  	news : {},
	  	user : {},
	  	added : false
	  };
	}

	componentDidMount() {
		this.getNews();
		this.getUserInfo();
	}

    getNews = () => {
    	const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
        var self = this;
        const args = {
            username : this.props.match.params.handle
        }
            soap.createClient(url, function(err, client){
                  client.getbyuser(args, function(err, result) {
                      if(JSON.parse(result.return).status ==200){
                        self.setState({news : JSON.parse(result.return).results})
                        self.setState({loading : false});
                        
                      }
                  });
              });
    }

    getUserInfo = () => {
    	const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/WS_TEST?wsdl';
        var self = this;
        const args = {
            username : this.props.match.params.handle
        }
            soap.createClient(url, function(err, client){
                  client.getinfo(args, function(err, result) {
                      if(JSON.parse(result.return).status ==200){
                        self.setState({user : JSON.parse(result.return)})
                        self.isFriend();
                      }
                  });
              });	
    }

    isFriend = () => {
    	const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/WS_TEST?wsdl';
        var self = this;
        const args = {
            username : localStorage.getItem('token')
        }
        soap.createClient(url, function(err, client){
              client.getinfo(args, function(err, result) {
                  if(JSON.parse(result.return).status ==200){
                    for (var i = 0; i < JSON.parse(result.return).friends.length; i++){
	                        console.log(JSON.parse(result.return).friends[i]);
	                      	if (JSON.parse(result.return).friends[i] === self.props.match.params.handle){
	                        	self.setState({added : true});
	                        	console.log(self.state.added);
                      		}
                    	}
                  }
              });
          });	
    	
    }

    follow = () => {
    	if(this.state.added)
    		this.deletefriend();
    	else this.addfriend();
    }

    addfriend = () => {
    	const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/WS_TEST?wsdl';
        var self = this;
        const args = {
            friend : this.props.match.params.handle,
            username : localStorage.getItem("token")
        }
        soap.createClient(url, function(err, client){
              client.addfriend(args, function(err, result) {
                  if(JSON.parse(result.return).status ==200){
                    self.setState({added : true});
                   
                  }
              });
          });
    
    }

    deletefriend = () => {
    	const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/WS_TEST?wsdl';
        var self = this;
        const args = {
            friend : this.props.match.params.handle,
            username : localStorage.getItem("token")
        }
        soap.createClient(url, function(err, client){
              client.deletefriend(args, function(err, result) {
                
                    self.setState({added : true});
                   
                  
              });
          });
    
    }

	render(){
		return (
			<div>
				<Container fixed style={{paddingTop:100}}>
					<Grid container style={{backgroundColor:"#4993D1", color:"#fff"}}>
						<Typography
		                    variant="h5"
		                    className="p-2"
		                >
		                    {this.state.user.firstname} {this.state.user.lastname}
		                </Typography>
		                <Button size="small" className="ml-auto my-2 mr-2"  style={{backgroundColor:"#fff",color:"#4993D1"}} color="primary" onClick={this.deletefriend} >
                        	{
		                	this.state.added ? "UnFollow":  "Follow"}
                        		</Button>
		                

                    </Grid>
                    	

                    
                    <Grid container >
	                    
	                        <Grid item xs={12}>
                           {this.state.loading ? (
                                <Typography style={{marginTop : 200}}>Loading...</Typography>
                            ) : (
                            this.state.news.map(news=> (
                                    <NewItem
                                        history={this.props.history}
                                        id = {news.id}
                                        key = {news.id}
                                        username = {news.username}
                                        title = {news.title}
                                        createdAt = {news.createdAt}
                                        content = {news.content}
                                        total_score = {news.total_score}
                                    />
                                ))
                                
                            )}
                        </Grid>
                    </Grid>



				</Container>
			</div>
			)
		
	}

}


export default User; 