/**
 * Shivaan Motilal
 * 10/08/2017
 * React Call Logger Application 
 */

import React, { Component } from 'react';
import {
  Alert,
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TextInput,
  ScrollView,
  TouchableHighlight,
  BackAndroid
} from 'react-native';

import { StackNavigator, } from 'react-navigation';  //navigation between screeens
import SectionListBasics from './components/SectionListBasics';
import CallLogAndroid from './components/RctActivity';
import Logs from './components/Logs';
import filter from './components/Filter';
import Login from './components/Login';

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

//Customise default label
const Label = (props) => {
    return (
        <Text 
            style={props.styles && props.styles.textLabel ? props.styles.textLabel : styles.textLabel}
        >
            {props.text}
        </Text>
    );
}

//Main Class displaying Login screen
export default class Call_Logger extends Component {

	static navigationOptions = { title: 'Login', };
	
  _onPress(){
     Alert.alert('You tapped the button!')
  }

  _sectionList(){
	<SectionListBasics />
  }

  _exitOnPress(){
	  BackAndroid.exitApp()
  }
  render() {
	const { navigate } = this.props.navigation;
    return (
        <ScrollView style={styles.scroll}>
			<Container>
			    <Button 
				label="No Login Details? Click here to Register"
				styles={{button: styles.alignRight, label: styles.label}} 
				onPress={this.onPress} />
			</Container>
			<Container>
			    <Label text="Username/Email" />
			    <TextInput
			        style={styles.textInput}
			    />
			</Container>
			{/* Adding password buttons */}
			<Container>
			    <Label text="Password" />
			    <TextInput
			        secureTextEntry={true}
			        style={styles.textInput}
			    />
			</Container>
			{/* Adding Sign In and CANCEL buttons */}
			<View style={styles.footer}>
			    <Container>
			        <Button 
			            label="Login"
			            styles={{button: styles.primaryButton, label: styles.buttonWhiteText}} 
						onPress={() => navigate('List', { name: 'Your Call Log' }) }
			            />
			    </Container>
			    <Container>
			        <Button 
			            label="CANCEL"
			            styles={{label: styles.buttonBlackText}} 
			            onPress={this._exitOnPress}
			            />
			    </Container>
			</View>

        </ScrollView>
    );
  }
}

const App = StackNavigator({ Login: { screen: Call_Logger }, List: { screen: SectionListBasics }, Logs: { screen: Logs}, Filter: {screen: filter}});  //adding screens to navigator

const styles = StyleSheet.create({
	button: {
	alignItems: 'center',
	justifyContent: 'center',
	padding: 20
	},
	
	labelContainer: {
	marginBottom: 20
	},

	textLabel: {
		fontSize: 20,
		fontWeight: 'bold',
		fontFamily: 'Verdana',
		marginBottom: 10,
		color: '#595856'
	},

	scroll: {
	    backgroundColor: '#E1D7D8',
	    padding: 30,
	    flexDirection: 'column'
	},

	label: {
	    color: '#0d8898',
	    fontSize: 20
	},

	alignRight: {
	    alignSelf: 'flex-end'
	},
	textInput: {
	    height: 80,
	    fontSize: 30,
	    backgroundColor: '#FFF'
	},

        buttonWhiteText: {
	    fontSize: 20,
	    color: '#FFF',
	},
        
	buttonBlackText: {
	    fontSize: 20,
	    color: '#595856'
	},

	primaryButton: {
	    backgroundColor: '#34A853'
	},

	footer: {
	   marginTop: 100
	}
});

AppRegistry.registerComponent('Call_Logger', () => App);
