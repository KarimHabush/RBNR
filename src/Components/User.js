import React, { Component } from "react";
import {Grid,Container,Typography,Divider,Button} from '@material-ui/core';
import NewItem from "./NewItem";

const soap = require("soap");



class User extends Component {

	constructor(props) {
	  super(props);
	
	  this.state = {
	  	loading : true,
	  	news : {}
	  };
	}

	componentDidMount() {
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

    addfriend = () => {
    	const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
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

	render(){
		return (
			<div>
				<Container fixed style={{paddingTop:100}}>
					<Grid container >
						<Typography
		                    variant="h5"
		                >
		                    @{this.props.match.params.handle}
		                </Typography>
		                <Button className="ml-auto" style={{backgroundColor:"#4993D1",color:"#fff"}} color="primary" onClick={this.addfriend} >
                            Add Friend
                        </Button>
                    </Grid>
                    <Divider className="mt-2 mb-2" />
                    <Divider className="mt-2 mb-2" />
                    
                    <Grid container >
                    <Typography
		                    variant="body1"
		                >
		                    Latest news : 
		                </Typography>
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