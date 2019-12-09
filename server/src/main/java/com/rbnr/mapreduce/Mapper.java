package com.rbnr.mapreduce;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


class IdSorter implements Comparator<Reaction>
{
    @Override
    public int compare(Reaction e1, Reaction e2) 
    {
        return e1.getNews_id().compareToIgnoreCase( e2.getNews_id());
    }
}


public class Mapper{

	public  Map<String, List<Integer>>  map(ResultSet reactions){
            List<Reaction> list = new ArrayList<>();
            Reaction reaction ;
            for(Row r : reactions){
                reaction = new Reaction();
                reaction.setNews_id(r.getUUID("id_news").toString());
                reaction.setScore(r.getInt("score"));
                list.add(reaction);
            }
            
            list.sort(new IdSorter());

            // create the thing to store the sub lists
            Map<String, List<Integer>> subs = new HashMap<String, List<Integer>>();

            // iterate through your objects
            for(Reaction o : list){

                // fetch the list for this object's id
                List<Integer> temp = subs.get(o.getNews_id());

                if(temp == null){
                    // if the list is null we haven't seen an
                    // object with this id before, so create 
                    // a new list
                    temp = new ArrayList<Integer>();

                    // and add it to the map
                    subs.put(o.getNews_id(), temp);
                }

                // whether we got the list from the map
                // or made a new one we need to add our
                // object.
                temp.add(o.getScore());
            }
            
            return subs;
	}

        
        
        
        
        public Map<String,Integer> reduce(Map<String, List<Integer>> mapres){
            Map<String,Integer> res = new HashMap<String,Integer>();
            for (Map.Entry<String,List<Integer>> scores : mapres.entrySet()){
                int result =0;

                for(int score : scores.getValue()){
                    result+=score;
                }
                res.put(scores.getKey(),result);

            }
            return res; 
        }


}