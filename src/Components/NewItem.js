import React, { Component } from "react";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import TimeAgo from "react-timeago";
import CommentIcon from "@material-ui/icons/Comment";
import ThumbsUpDownIcon from '@material-ui/icons/ThumbsUpDown';


class NewItem extends Component {

    constructor() {
      super();
    
      this.state = {
        likeClicked : false,
        dislikeClicked : false,
      };

    }

    


    render() {
        return (
            <div onClick={() => {this.props.history.push("/news/"+this.props.id)}}>
                <Card className="newscard my-3"   >
                    <CardContent>
                        <Grid container alignItems="center">
                            
                            <Typography
                                variant="body1"
                                style={{
                                    color: "#4993D1",
                                    marginRight: 3
                                }}
                            >
                                @{this.props.username}
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
                            <Typography
                                
                                style={{ fontSize:12,color: "#777777" }}
                            >
                            {this.props.createdAt}
                            </Typography>
                            .
                        </Grid>
                        <Grid container>
                            <Typography  component="h2" style={{fontSize : 22}} >
                                {this.props.title}
                            </Typography>
                        </Grid>

                        <Grid container >
                            <Typography  component="p" style={{textAlign:"left"}} >
                                {this.props.content}
                            </Typography>
                        </Grid>

                    </CardContent>

                    <CardActions disableSpacing className="bg-light">
                        <Button

                            size="small"
                            style={{ fontSize: 10 }}
                            
                        >
                            {
                                this.props.total_score<0 ? 
                                (
                                    <ThumbsUpDownIcon style={{ color: "#ea526f", marginRight: 4 }}/>
                                    ):(
                                    <ThumbsUpDownIcon style={{ color: "#1b998b", marginRight: 4 }}/>
                                    )
                            }

                            
                             {this.props.total_score ? this.props.total_score : 0}
                        </Button>
                        {/*<Button
                            aria-label="Delete"
                            size="small"
                            style={{ fontSize: 10 }}
                            
                        >
                                <ThumbUpAltIcon


                                style={{ color: "#1b998b", marginRight: 3 }}
                            />
                            
                            Like
                        </Button>

                        <Button
                            aria-label="Delete"
                            size="small"
                            style={{ fontSize: 10 }}
                            
                        >
                                <ThumbDownAltIcon
                                style={{ color: "#ea526f", marginRight: 3 }}
                            />
                            
                            Dislike
                        </Button>
                        */}
                        <Button
                            aria-label="Delete"
                            size="small"
                            style={{ fontSize: 10 }}
                            
                        >
                            <CommentIcon
                                style={{ color: "#70d2d3", marginRight: 3 }}
                            />
                            Comments
                        </Button>

                        

                        

                    </CardActions>
                </Card>

            </div>
        );
    }
}

export default NewItem;
