import React, { Component } from 'react';
import { NativeModules, NativeEventEmitter, AppRegistry, SectionList, StyleSheet, Text, View,ScrollView, WebView, WritableMap, WritableArray, TouchableHighlight } from 'react-native';
import { StackNavigator, } from 'react-navigation';
import CallLogAndroid from './RctActivity';

//Customise default button
const Button = (props) => {
    function getContent(){
        if(props.children){
            return props.children;
        }
        return <Text style={props.styles.label}>{props.label}</Text>
    }

    return (
        <TouchableHighlight 
            underlayColor="#ccc"
            onPress={props.onPress} 
            style={[
                props.noDefaultStyles ? '' : styles.button, 
                props.styles ? props.styles.button : '']}
        >
            { getContent() }
        </TouchableHighlight>
    );
}
//Customise default container
const Container = (props) => {
    return (
        <View style={styles.labelContainer}>
            { props.children }
        </View>
    );
}
export default class SectionListBasics extends Component {
  static navigationOptions = { title: 'Options', }
  constructor(props) {
      super(props)
      var today= new Date();
      this.props={
        text: []
      }
      this.state = { 
        log: [] ,
        callLog: null,
        duration: "",
        number:""
      }
      this.allLogs();
      this.fetchLogs();
  }

  //Fetches entire log as a printable string
  async allLogs() {
      var wlog= await CallLogAndroid.returnLogs(100,100);
      this.setState({callLog: wlog})
  }

  //Fetches array of maps which are JSON objects inherently
  async fetchLogs() {
      var log= await CallLogAndroid.alert(100,100);
      this.setProps({text: log})
      this.setState({log: log})
  }

  render() {
    const { navigate } = this.props.navigation;
    return (
        <ScrollView style={styles.scroll}>
          <View style={{
                  flex: 1,
                  flexDirection: 'column',
                  justifyContent: 'space-between',
                }}>
                 <Button label='Click for Call Log' styles={{button: styles.primaryButton, label: styles.buttonBlackText}} onPress={() => navigate('Logs', { name: 'Call Log List' }) }
                />
                {/* Cannot seem to separate these two buttons in application tried <br> and containers*/}
                <Button label='Click for Last 3 days calls' styles={{button: styles.secondaryButton, label: styles.buttonBlackText}} onPress={() => navigate('Filter', { name: 'Filtered Calls' }) }
                />
          </View>
        </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  item: {
    padding: 10,
    fontSize: 18,
    height: 44,
  },
  scroll: {
	    backgroundColor: '#E1D7D8',
	    padding: 30,
	    flexDirection: 'column'
	},
  primaryButton: {
	  backgroundColor: '#add8e6'
  },
  buttonBlackText: {
	    fontSize: 20,
	    color: '#595856'
	},
  secondaryButton:{
     backgroundColor: '#ffff00',
  },
  label: {
	    color: '#0d8898',
	    fontSize: 20
	}
})
