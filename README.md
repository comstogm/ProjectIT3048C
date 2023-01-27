# My Calorie Counter

---

Design Document

Anastasiia Efimova  
Daniel West  
Elizabeth Bissinger  
Gabriel Comstock  

## Introduction

Are you having trouble keeping track of your calories? Do you struggle to remember to log your calories? My Calorie Counter can help you:  

-	Record your calories for each meal or day  
-	Receive reminders to log calories based on time and location  

Using My Calorie Counter will allow you to track your calories along with receive alerts to remind you to log your meals.

## Storyboard

![Dashboard4](https://user-images.githubusercontent.com/77344568/215071273-1faba0b1-a6f4-4b5a-9180-1ed7c455a854.png)

## Functional Requirements

### Requirement 1: Log Meal

#### Scenario
As a user wanting to log my calories, I want to be able to log my individual meals within a day.  

#### Dependencies
Food search data is available and accessible/Food calorie data is storable and accessible.  

#### Assumptions
Food is stated in English  
Calories are stated as a decimal  

#### Examples
#### 1.1
**Given** a feed of food/calorie data is available and accessible  
**When** I log an egg as 70 calories for lunch  
**Then** the item and calories will be stored and displayed on the user screen, and the calories will be added to the meal total and daily total.  

#### 1.2
**Given** a feed of food/calorie data is available  
**When** I search for cabbbage to log  
**Then** I should receive an error message “no search results for ‘cabbbage’”  

### Requirement 2: Reminder

#### Scenario
As a user who forgets to track calories, I want to be reminded to log calories  

#### Dependencies
Food/calorie data is available and accessible  
The user has granted location/time access  
The user has granted notifications  

#### Assumptions
Food is stated in English  
Calories are stated as a decimal  

#### Examples
#### 2.1
**Given** a feed of food/calorie data is available  
**When** The user has entered calories for dinner  
**Then** no reminder notification is sent  

#### 2.2
**Given** a feed of food/calorie data is available  
**When** The user has not logged a meal or calories for dinner  
**Then** a reminder notification is sent “Don’t forget to log your meal for dinner”  

## Class Diagram

![IT3048C drawio](https://user-images.githubusercontent.com/77344568/215071420-94d46c3a-6820-43a0-abdf-ff9ec7c7325e.png)

## Class Diagram Description

**MainActivity:** This screen contains the main functionality of the app with the ability to access the calendar to log food items, view reports, and any other core functions.  
**RetrofitClientInstance:** Bootstrap class required for Retrofit.  
**FoodData:** Noun class that represents varieties of food.  
**Food:** Noun class that represents an individual food item for logging.  
**IFoodDataDAO:** Interface for Retrofit to find and parse Food JSON.  
**IFoodDAO:** Interface for Room Database to store food data.  

## Scrum Roles

- Product Owner: Gabriel Comstock  
- Scrum Master: Liz Bissinger  
- Frontend Developer: Anastasiia Efimova  
- Integration Developer: Daniel West  

## Weekling Meeting(s)

Monday at 7:30 PM on Teams.  
If needed, a second meeting will be held Friday at 7:30 PM on Teams.
