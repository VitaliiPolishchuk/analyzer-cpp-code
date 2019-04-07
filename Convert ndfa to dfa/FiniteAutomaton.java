package com.company;

import java.sql.Array;
import java.util.*;

public class FiniteAutomaton {
    Map<String,ArrayList<List<String>>> dictNDFA = new HashMap<String,ArrayList<List<String>>>();
    Map<String,ArrayList<List<String>>> dictDFA = new HashMap<String,ArrayList<List<String>>>();

    boolean deterministic = false;

    int counterDeadState;

    Map<String,String> dictAlphabet = new HashMap<String,String>();

    public FiniteAutomaton(FileStructure fileStructure){
        //initiate alphabet
        String lineAlphabet = fileStructure.getAlphabet();
        String[] letterAlphabet = lineAlphabet.split(" ");
        int indexLetter = 0;
        for (String letter : letterAlphabet) {
            dictAlphabet.put(letter,Integer.toString(indexLetter++));
        }

        //initiate states
        String lineStates = fileStructure.getStates();
        String[] states = lineStates.split(" ");
        int indexStates = 0;
        for (String state : states) {

            ArrayList<List<String>> emptyArrStateDirected = new ArrayList<List<String>>();

            for (String letter : letterAlphabet) {
                List<String> emptyListArrivalStates = new LinkedList<String>();
                emptyArrStateDirected.add(Integer.parseInt(dictAlphabet.get(letter)),emptyListArrivalStates);
            }

            this.dictNDFA.put(state, emptyArrStateDirected);
        }

        for (String transaction: fileStructure.transactions){
            String[] transElement = transaction.split(" ");
            ArrayList<List<String>> arr = dictNDFA.get(transElement[0]);
            List<String> list = arr.get(Integer.parseInt(dictAlphabet.get(transElement[1])));
            list.add(transElement[2]);
        }
    }

    //dont forget about NDFA and DFA
    public List<String> getTransaction(){
        List<String> transactions = new LinkedList<String>();

        for(String state: dictDFA.keySet()){
            for(String letter: dictAlphabet.keySet()){
                String transaction = new String();
                List<String> list = dictDFA.get(state).get(Integer.parseInt(dictAlphabet.get(letter)));
                if(!list.isEmpty()) {
                    transaction += state + " " + letter + " " + list.get(0);
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }
//dont forget about NDFA and DFA
    public String getStates(){
        String states = new String();

        for (String state: dictDFA.keySet()){
            states += state + " ";
        }

        return states;
    }

    public void determine(){
        //initializate DFA
        if(!deterministic){
            for (String state : dictDFA.keySet()) {

                ArrayList<List<String>> emptyArrStateDirected = new ArrayList<List<String>>();

                for (String letter : dictAlphabet.keySet()) {
                    List<String> emptyListArrivalStates = new LinkedList<String>();
                    emptyArrStateDirected.add(Integer.parseInt(dictAlphabet.get(letter)),emptyListArrivalStates);
                }

                this.dictNDFA.put(state, emptyArrStateDirected);
            }

            this.setCounterDeadState(dictNDFA.keySet().size());

//            Set<String> e = new HashSet<String>();
//            e.addAll(this.dictNDFA.keySet());

//            for (String state : e) {
//
//                ArrayList<List<String>> arrStateDirected = this.dictNDFA.get(state);
//                for (String letter : this.dictAlphabet.keySet()) {
//                    List<String> listArrivalStates = arrStateDirected.get(Integer.parseInt(dictAlphabet.get(letter)));
//
//                    int listArrivalStatesSize = listArrivalStates.size();
//
//                    if (listArrivalStatesSize < 1) {
//                        //create method for get number of dead statement
//                        String newStateName = Integer.toString(this.getCounterDeadState());
//                        this.dictNDFA.get(state).get(Integer.parseInt(dictAlphabet.get(letter))).add(newStateName);
//                        this.createDeadState(newStateName);
//                    }
//                }
//            }

            //convertion to DFA
            for (String state : this.dictNDFA.keySet()) {

                ArrayList<List<String>> arrStateDirected = this.dictNDFA.get(state);
                ArrayList<List<String>> newArrStateDirected = new ArrayList<List<String>>();
                for (String letter : this.dictAlphabet.keySet()) {
                    List<String> listArrivalStates = arrStateDirected.get(Integer.parseInt(dictAlphabet.get(letter)));
                    List<String> newListArrivalStates = new LinkedList<String>();

                    int listArrivalStatesSize = listArrivalStates.size();

                    if (listArrivalStatesSize == 1) {
                        newListArrivalStates.add(listArrivalStates.get(0));
                    } else if (listArrivalStatesSize > 1) {
                        String newStateName = new String();

                        for (String st : listArrivalStates) {
                            newStateName += st;
                        }

                        newListArrivalStates.add(newStateName);

                        this.createState(newStateName);
                    }  else {
                        //create method for get number of dead statement
                        String newStateName = Integer.toString(this.getCounterDeadState());

                        newListArrivalStates.add(newStateName);

                        this.createDeadState(newStateName);
//                        this.dictNDFA.get(state).get(Integer.parseInt(dictAlphabet.get(letter))).add(newStateName);
                    }
                    newArrStateDirected.add(newListArrivalStates);
                }
                dictDFA.put(state, newArrStateDirected);
            }
            deterministic = true;
        }
    }

    public void createState(String newStateName){
        if(!newStateName.equals("")) {
            String[] statesNameForNewState = newStateName.split("");
            ArrayList<List<String>> newArrStateDirected = new ArrayList<List<String>>();

            for (String letter : dictAlphabet.keySet()) {
                Set<String> setNameState = new HashSet<String>();

                for (String state : statesNameForNewState) {
                    List<String> listArrivalStates = this.dictNDFA.get(state).get(Integer.parseInt(dictAlphabet.get(letter)));

                    for (String st : listArrivalStates) {
                        setNameState.add(st);
                    }
                }
                String newStateNameToList = new String();

                for (String state : setNameState) {
                    newStateNameToList += state;
                }

                List<String> newListArrivalState = new LinkedList<String>();
                newListArrivalState.add(newStateNameToList);
                newArrStateDirected.add(newListArrivalState);
            }
            this.dictDFA.put(newStateName, newArrStateDirected);

            //upgrate dict (chech existing Arrival state in keySet)
            for (String letter : dictAlphabet.keySet()) {
                //if state exist in keySet
                String nameState = newArrStateDirected.get(Integer.parseInt(dictAlphabet.get(letter))).get(0);
                if (!dictDFA.keySet().contains(nameState)) {
                    this.createState(nameState);
                }
            }
        }

    }

    public void createDeadState(String newStateName){
        ArrayList<List<String>> newArrStateDirected = new ArrayList<List<String>>();

        for (String letter: dictAlphabet.keySet()){

            String newStateNameToList = newStateName;

            List<String> newListArrivalState = new LinkedList<String>();
            newListArrivalState.add(newStateNameToList);
            newArrStateDirected.add(newListArrivalState);
        }
        this.dictDFA.put(newStateName, newArrStateDirected);
//        this.dictNDFA.put(newStateName, newArrStateDirected);

    }

    private int getCounterDeadState(){
        this.counterDeadState++;
        while(this.dictDFA.keySet().contains(Integer.toString(this.counterDeadState))){
            this.counterDeadState++;
        }
        return this.counterDeadState;
    }

    private void setCounterDeadState(int num){
        this.counterDeadState = num;
    }
}