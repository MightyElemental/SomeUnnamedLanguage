# SomeUnnamedLanguage
An interpreter for an unnamed language. This language is supposed to be close to natural language. Don't expect efficient syntax as it is quite verbose.

The initial purpose of this language was to be a super-basic scripting language for a game I was working on. It was to allow in-game scripting for automating tasks. However, it likely won't be used for that purpose anymore and will instead be a stand-alone language.

Below are examples of code, but please note they are subject to change and may not reflect the best method for the intended result.

## Example code
### Hello World
```
display "Hello World!"
```
```
Hello World!
```
### Variables
#### Numbers
```
set :num1 to 5
add 10 to :num1
display "5 + 10 = " :num1
add 7.5 to :num1
display "15 + 7.5 = " :num1
```
```
5 + 10 = 15
15 + 7.5 = 22.5
```
#### Strings
```
set :name to "John"
set :name to :name " Doe"
display "Hello " :name
```
```
Hello John Doe
```

* * *
# Syntax Documentation

Please note that not all syntax/commands listed here are fully implemented yet.

* All variables must start with '``:``', such as ``:name``.
* Strings are any characters contained within quotes (``"``), e.g. ``"string"``.

#### Cheat sheet for this documentation
* Generic numbers will be represented as a single letter ``x``.
* Generic variables will be called ``:var``, followed by a number where needed.
* Generic strings will be ``"string"``.

## Comments
Comments start with a single hash ``#``. (For now, comments need to be on their own line)
```
# this is a comment
```

## Addition
Adds a number to a variable. For adding a value to a string or a list, see ``append``.
```
add x to :var
```

## Display / Print
The display command allows combinations of numbers, strings, and variables. Each need to be separated by one or more space.
```
display x
display "string"
display "string" :var
```

## Set Variable
Set a variable to a value. The value can be a number, string, boolean, list, another variable, or a combination (excluding lists).
```
1. set :var to x
2. set :var to "string"
3. set :var to "string" :var
4. set :var to :var2
5. set :var to 1,2,3,4,5
```
1. Creates a number variable
2. Creates a string variable
3. Creates a string variable
4. Creates a variable of the same type as ``:var2``
5. Creates a list variable [WIP]

## Lists [WIP]
Lists in this case are actually a combination of arrays and dictionaries. They can be used like arrays, but you can also assign key/value pairs.
The array mode is index zero.

Examples of lists:
```
# create a new list with values 1, 2, 3, 4, 5
set :var to 1,2,3,4,5

# outputs index 3 of :var (4 in this case)
display element 3 of :var

# assigns key "name" to value "John" in variable :var
append "John" to element "name" of :var

# adds the number 5 to the next available index in :var
append 5 to :var
```
How to address lists
```
# select value assigned to key "name" from :var
element "name" of :var

# select index 5 from :var
element 5 of :var

# select value assigned to key :var2 from :var
element :var2 of :var
```





