import React, { Component } from "react";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";
import Tooltip from "@material-ui/core/Tooltip";
import Fab from "@material-ui/core/Fab";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import NewItem from "./NewItem";
import DialogTitle from "@material-ui/core/DialogTitle";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Avatar from "@material-ui/core/Avatar";
import IconButton from "@material-ui/core/IconButton";
import FormControl from "@material-ui/core/FormControl";
import FormHelperText from "@material-ui/core/FormHelperText";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Button from "@material-ui/core/Button";
import AddIcon from "@material-ui/icons/Add";
import InputAdornment from '@material-ui/core/InputAdornment';
import ArtTrackIcon from '@material-ui/icons/ArtTrack';


const soap = require('soap');

class Newsfeed extends Component {
    constructor() {
        super();

        this.state = {
            open: false,
            errors:{},
            news : {},
            loading : true,
            newloading: false
        };

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }
    onChange = e => {
        this.setState({ [e.target.name]: e.target.value });
    };

    onSubmit = e => {
        e.preventDefault();
        const args = {
            title : this.state.title,
            content : this.state.content,
            username : localStorage.getItem("token")
        };
        const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
        this.setState({newloading : true});

        var self = this;
        soap.createClient(url, function(err, client){
              client.addnew(args, function(err, result) {
                    self.setState({newloading : false});
                    window.location.reload();
              });
        });
    };

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    componentDidMount() {
        if(!localStorage.token)
            this.props.history.push("/login");
        else{
            const url = 'http://localhost:8080/RBNR-1.0-SNAPSHOT/NEW_WS?wsdl';
        var self = this;
        const args = {}
            soap.createClient(url, function(err, client){
                  client.getall(args, function(err, result) {
                      if(JSON.parse(result.return).status ==200){
                        self.setState({news : JSON.parse(result.return).results})
                        self.setState({loading : false});
                        
                      }
                  });
              });
        }
    }

    render() {
        return (
            <div  style={{backgroundColor:"#fafafa"}}>
                <Container fixed style={{ paddingTop: 100 }}>
                    

                            <TextField
                                className="w-100"
                              placeholder="Got some news? We hope they're good..."
                              id="outlined-start-adornment"
                              onClick={this.handleClickOpen}
                              InputProps={{
                                endAdornment: <InputAdornment position="start"><ArtTrackIcon/></InputAdornment>,
                              }}
                              variant="outlined"
                            />

                    <form onSubmit={this.onSubmit}>
                        <Dialog
                            maxWidth="sm"
                            fullWidth={true}
                            open={this.state.open}
                            onClose={this.handleClose}
                            aria-labelledby="form-dialog-title"
                        >
                            <DialogTitle id="form-dialog-title" style={{backgroundColor:"#4993D1",color:"#fff"}}>
                                Share news
                            </DialogTitle>
                            <DialogContent>
                                    <TextField
                                        style={{backgroundColor:"#fafafa"}}
                                        id="title"
                                        name="title"
                                        className="w-100"
                                        label="title"
                                        value={this.state.title}
                                        onChange={this.onChange}
                                        variant="outlined"
                                        required
                                        aria-describedby="title-error-text"
                                    />
                                    <TextField
                                        style={{backgroundColor:"#fafafa"}}
                                        variant="outlined"
                                        multiline={true}
                                        rows={5}
                                        className="w-100 mt-3"
                                        id="content"
                                        name="content"
                                        label="content"
                                        value={this.state.content}
                                        onChange={this.onChange}
                                        required
                                        aria-describedby="content-error-text"
                                    />
                            </DialogContent>
                            <DialogActions>
                                <Button
                                    onClick={this.handleClose}
                                    color="primary"
                                >
                                    Cancel
                                </Button>
                                <Button onClick={this.onSubmit} style={{backgroundColor:"#4993D1",color:"#fff"}} color="primary" disabled={this.state.newloading}>
                                    {
                                        this.state.newloading ? "Loading..." : "Post"
                                    }
                                </Button>
                            </DialogActions>
                        </Dialog>
                    </form>


                    <Grid container justify="center">
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
        );
    }
}

export default Newsfeed;
