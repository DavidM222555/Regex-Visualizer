# Regex-Visualizer
JavaFX application for visualizing a regex in action.

# Basics and Implementation Details of Project
This project was developed out of my interest to explore how programs like GREP work under the hood. In this project I implemented a string search algorithm that can be given a regular expression of a form that utilizes the following conventions: + for union, * for the Kleene Star (Closure), and where concatenation is implicit (but is made explicit as a preprocessing step for the NFA construction). From there this algorithm takes the infix given input and converts it to postfix notation using the famous Shunting-Yard algorithm (https://en.wikipedia.org/wiki/Shunting_yard_algorithm) and performs the converstion of the regex string to an NFA through Thompson's construction (https://en.wikipedia.org/wiki/Thompson%27s_construction).


# Sample Images
![RegexPicture1](https://user-images.githubusercontent.com/46248699/175829712-5752c61f-7940-4d10-9885-24893254e824.PNG)

# TODO
- Support for wildcard characters (typically denoted .)
- Support for escape characters (how to match '+' and other characters used by our regex for special purposes)
- Make it prettier ;;
