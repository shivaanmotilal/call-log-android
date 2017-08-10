import React, { Component } from 'react';
import { NativeModules, NativeEventEmitter, AppRegistry, SectionList, StyleSheet, Label, Text, TextInput, View,ScrollView, WebView, Button, WritableMap, WritableArray } from 'react-native';
import { StackNavigator, } from 'react-navigation';
import CallLogAndroid from './RctActivity';
//import Moment from 'moment';

export default class filter extends Component {
  static navigationOptions = { title: 'Calls in Last 3 days', }

  constructor(props) {
      super(props) 
      this.props={
        text: []
      }
      this.state = { 
        log: [] ,
        callLog: null,
        duration: "",
        number:"",
        today:""
      }
      this.fetchLogs();
  }

  async fetchLogs() {
      var log= await CallLogAndroid.alert(100,100);
      this.setState({log: log})
      this.setState({today: new Date()})
  }

  render() {
    return (<View className='app'>
        <ScrollView style={styles.scroll}>
              {/* Parse Call logs in variable */}
             {this.state.log.map((y) => { 
                        
                        var today= new Date();
                        var day = new Date( today.getTime()- (3*(86400000) ) );  //Subtract three days from system date (86400000 milliseconds in one day)
                        var date=new Date(y.year,y.month,y.day);
                        var msg= "";
                        if(date.getTime()>day.getTime()){    //current date greater than 05/07/2017
                            
                            msg="Date : "+date.toDateString()+"\n"+"Type : "+y.type+"\n"+"Number : "+y.number+"\n"+"Duration : "+y.duration+"\n.\n.\n.\n.";

                        }

                        else{
                            //Do nothing
                        }
                        //var m= moment(date);
                        //var isvalid=m.isValid();
                        //var isafter= m.isAfter('2014-03-24T01:14:00Z');
                        {/* var check="";
                        if(isafter === true){
                            console.log('is after true');
                            var check= 'date is after that specified';
                        } else {
                            console.log('is after is false');
                            var check='date is not after that specified';
                        } */}
                        {/* if ()   */}
                        {/* var elapsed = date.geTime(); */}
                        //console.log(date.toDateString()); 
                        //return (<Text style={styles.titleText}>{msg}</Text>);  
                        return (<Text style={styles.titleText}>{msg} </Text>);
                }) } 
            </ScrollView>
      </View>);
  }
}

const styles = StyleSheet.create({
  container: {
   flex: 1,
   paddingTop: 22
  },

  titleText: {
    fontSize: 20,
    fontWeight: 'bold',
  },
  
  textInput: {
	    height: 80,
	    fontSize: 30,
	    backgroundColor: '#FFF'
	},
})