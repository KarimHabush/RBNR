import React, { Component } from "react";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";

import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";

import DialogTitle from "@material-ui/core/DialogTitle";
import Grid from "@material-ui/core/Grid";
import TimeAgo from "react-timeago";
import CloseIcon from "@material-ui/icons/Close";
import ForumIcon from "@material-ui/icons/Forum";
import TextField from "@material-ui/core/TextField";
import ThumbUpAltIcon from '@material-ui/icons/ThumbUpAlt';
import ThumbDownAltIcon from '@material-ui/icons/ThumbDownAlt';
const soap = require("soap");

class Newspage extends Component {
    constructor() {
        super();
        this.state = {
            comment: "",
            news:{},
            loading:true,
            liked:false,
            disliked:false,
            commentloading : false,
        };
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.react = this.react.bind(this);
    }
    onChange = e => {
        this.setState({ [e.target.name]: e.target.value });
    };

    onSubmit = e => {
        e.preventDefault();
        this.setState({commentloading : true})
        const args = {
            id : this.props.match.params.handle,
            username : localStorage.getItem("token"),
            comment : this.state.comment
        };

        const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
        var self = this;
        soap.createClient(url, function(err, client){
              client.addcomment(args, function(err, result) {
                    self.setState({commentloading : false});
              });
          });   


    };




    react = (score) => {
        const args = {
            id : this.props.match.params.handle,
            username : localStorage.getItem("token"),
            score : score
        };

        const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
        var self = this ;
        if(score===-1){
            if(this.state.disliked){
                soap.createClient(url, function(err, client){
                      client.deletereaction(args, function(err, result) {
                                    self.setState({disliked : !self.state.disliked})
                      });
                  });       
            }
            else {
                soap.createClient(url, function(err, client){
                      client.addreaction(args, function(err, result) {
                                    self.setState({disliked : !self.state.disliked});
                                    self.setState({liked : false});
                      });
                  });          
            }
        }

         if(score===1){
            if(this.state.liked){
                soap.createClient(url, function(err, client){
                      client.deletereaction(args, function(err, result) {

                            self.setState({liked : !self.state.disliked});
                      });
                  });       
            }
            else {
                soap.createClient(url, function(err, client){
                      client.addreaction(args, function(err, result) {
                            self.setState({liked : !self.state.disliked})
                            self.setState({disliked : false});
                      });
                  });          
            }
        }

        
    }


    componentDidMount() {
        
        const args = {
            id : this.props.match.params.handle,
        };

        const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
        this.setState({newloading : true});

        var self = this;
            soap.createClient(url, function(err, client){
                  client.getone(args, function(err, result) {
                             if(JSON.parse(result.return).status ===200){
                                self.setState({news : JSON.parse(result.return).results})
                                for (var i = 0; i < self.state.news.reactions.length; i++){
                                  if (self.state.news.reactions[i].username === localStorage.getItem('token')){
                                    if(self.state.news.reactions[i].score ===1)
                                        {self.setState({liked : true});}
                                    else
                                        {self.setState({disliked : true});}
                                    break;
                                  }
                                }
                            }
                  });
              });
    }


    handleClose = () => {
        this.props.history.goBack();
    };
    render() {
        return (
            <div>
                <Dialog
                    scroll="body"
                    fullWidth={true}
                    maxWidth="lg"
                    open={true}
                    onClose={this.handleClose}

                >
                    <DialogTitle className="bg-light" style={{borderLeft:"5px solid #4993D1"}}>
                        <Grid container alignItems="center">
                            
                            <Typography
                                variant="body1"
                                style={{
                                    color: "#4993D1",
                                    marginRight: 3
                                }}
                            >
                                @{this.state.news.username}
                            </Typography>
                            <Typography
                                variant="body1"
                                style={{
                                    color: "#aaaaaa",
                                    marginRight: 3
                                }}
                            >
                                shared some news
                            </Typography>
                            <TimeAgo
                                minPeriod="30"
                                style={{ color: "#777777",fontSize:13 }}
                                date={this.state.news.createdAt}
                            />
                            .
                            <Button
                                style={{ marginLeft: "auto" }}
                                edge="start"
                                color="inherit"
                                onClick={this.handleClose}
                                aria-label="Close"
                            >
                                Close
                                <CloseIcon />
                            </Button>
                        </Grid>
                    </DialogTitle>

                    <DialogContent style={{borderLeft:"5px solid #4993D1"}}>
                        
                        <Grid container>
                            <Typography  component="h2" style={{fontSize : 22}} >
                                {this.state.news.title}
                            </Typography>
                        </Grid>

                        <Grid container >
                            <Typography  component="p" style={{textAlign:"left",color:"#777777"}} >
                                ¶ {this.state.news.content}
                            </Typography>
                        </Grid>
                         
                        
                    </DialogContent>

                    <DialogContent style={{borderLeft:"5px solid #4993D1"}}>
                        <Grid container >
                            <Typography style={{fontSize:12}}> — Liked by : 
                        </Typography>
                            <Typography  component="p" style={{textAlign:"left",color:"#4993D1",fontSize:12}} >
                                {this.state.news.reactions ? 
                            (this.state.news.reactions.slice(0,3).map(reaction =>
                             ( reaction.score===1 ? ("@"+ reaction.username + ", "):null))):null}

                              and more...        
                            </Typography>
                        </Grid>
                    </DialogContent>
                    <DialogContent style={{borderLeft:"5px solid #4993D1"}}>


                        <Grid container>

                        <Button
                            aria-label="Delete"
                            size="small"
                            style={{ fontSize: 10 }}
                             onClick={()=> this.react(1)}
                        >
                                
                            {this.state.liked ? (
                                <ThumbUpAltIcon


                                style={{ color: "#1b998b", marginRight: 3 }}
                            />
                            ):(<ThumbUpAltIcon


                                style={{ color: "#aaaaaa", marginRight: 3 }}
                            />)}
                            Like
                        </Button>

                        <Button
                            aria-label="Delete"
                            size="small"
                            style={{ fontSize: 10 }}
                            onClick={()=> this.react(-1)}
                        >
                            {this.state.disliked ? (
                                <ThumbDownAltIcon


                                style={{ color: "#ea526f", marginRight: 3 }}
                            />
                            ):(<ThumbDownAltIcon


                                style={{ color: "#aaaaaa", marginRight: 3 }}
                            />)}
                            Dislike
                        </Button>
                        
                           
                        
                        
                            
                        
                        </Grid>
                    </DialogContent>
                    <DialogContent style={{borderLeft:"5px solid #4993D1"}}>
                        <Grid container>
                        
                        <TextField
                            variant="outlined"
                            multiline={true}
                            className="w-100 my-3 bg-light"
                            id="comment"
                            name="comment"
                            label="Write a comment..."
                            value={this.state.comment}
                            onChange={this.onChange}
                            required
                            aria-describedby="comment-error-text"
                        />
                            <Button
                                variant="outlined"
                                style={{ marginLeft: "auto" }}
                                aria-label="Submit answer"
                                onClick={this.onSubmit}
                                disabled = {this.state.commentloading}
                            >
                                {this.state.commentloading? "Loading..." : "Send"}
                            </Button>
                        </Grid>
                    </DialogContent>

                    <DialogContent style={{borderLeft:"5px solid #4993D1",backgroundColor:"#fafafa"}}>
                        <Typography className="my-2">Comments : </Typography>


                        {this.state.news.comments? (
                            this.state.news.comments.replace('[(','').replace(')]','').split('), (').map(comment => (
                                 <Grid container alignItems="center">
                            
                            <Typography
                                variant="body1"
                                style={{
                                    color: "#4993D1",
                                    marginRight: 3
                                }}
                            >
                                @{comment.replace(/'/g,'').split(',')[0]}
                            </Typography>
                            <Typography
                                variant="body2"
                                style={{
                                    color: "#aaaaaa",
                                    marginRight: 3
                                }}
                            >
                                commented
                            </Typography>
                            <Typography
                                style={{ color: "#777777",fontSize:13 }}
                            >
                            {comment.replace(/'/g,'').split(',')[1]}
                            </Typography>
                        </Grid>
                                ))
                            ):(

                            <CardContent>
                                <Grid
                                    container
                                    alignItems="center"
                                    justify="center"
                                >
                                    <ForumIcon
                                        style={{
                                            color: "#aaaaaa",
                                            width: 40,
                                            height: 40
                                        }}
                                    />
                                </Grid>
                                <Grid
                                    container
                                    alignItems="center"
                                    justify="center"
                                >
                                    <Typography
                                        style={{
                                            color: "#aaaaaa"
                                        }}
                                        variant="h6"
                                    >
                                        Be the first to write an answer.
                                    </Typography>
                                </Grid>
                            </CardContent>
                            )}

                    </DialogContent>
                </Dialog>
            </div>
        );
    }
}

export default Newspage;
