import React, { Component } from 'react';
import { NativeModules, NativeEventEmitter, AppRegistry, SectionList, StyleSheet, Text, View,ScrollView, WebView, Button, WritableMap, WritableArray } from 'react-native';
import { StackNavigator, } from 'react-navigation';
import CallLogAndroid from './RctActivity';

export default class callLog extends Component {
  static navigationOptions = { title: 'Your Call Logs (Please wait a few seconds for load)', }
  constructor(props) {
      super(props)
      this.state = { callLog: null}
      this.allLogs();
  }

  async allLogs() {
      var wlog= await CallLogAndroid.returnLogs(100,100);
      //console.log("before return",log);
      this.setState({callLog: wlog})
      //console.log(this.state.log);
  }

  render() {
    return (<View className='app'>
        <ScrollView style={styles.scroll}><Text style={styles.titleText}>{this.state.callLog}</Text></ScrollView>
      </View>);
  }
}

const styles = StyleSheet.create({
  container: {
   flex: 1,
   paddingTop: 22
  },

  titleText: {
    fontSize: 15,
  },

  sectionHeader: {
    paddingTop: 2,
    paddingLeft: 10,
    paddingRight: 10,
    paddingBottom: 2,
    fontSize: 20,
    fontWeight: 'bold',
    backgroundColor: 'rgba(247,247,247,1.0)',
  },
  
  item: {
    padding: 10,
    fontSize: 18,
    height: 44,
  },
})