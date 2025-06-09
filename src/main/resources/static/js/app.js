let textBox = document.getElementById("textBox"); //A fő különbség a hatókörre vonatkozó szabályok. A var kulcsszóval deklarált változók a közvetlen függvénytestre (tehát a függvény hatókörére), míg a let változók a { } által jelölt közvetlen záró blokkra (tehát a blokk hatókörére) vonatkoznak.
let messageBox = document.getElementById("messageBox");
let greenText = document.getElementById("green-text");
let yellowText = document.getElementById("yellow-text");
let redText = document.getElementById("red-text");
let btnGreen = document.getElementById("btn-green");
let btnYellow = document.getElementById("btn-yellow");
let btnRed = document.getElementById("btn-red");

let numberArray = new Array();
let numString;
let firstPhase = true;
let isCardCode = true;
let isPinCode = false;
let customer = true;
let isDeposit = false;
let isWithdraw = false;
let cardNumber;
let pinCode;
let responseMessage;
let amountOfMoney;



messageBox.innerHTML = "Please enter card number"; //HTML-ben deklarálva

function btnPush(n) //HTML-ben hívódik meg: onclick 1-9
{
    if(firstPhase && isCardCode) //első fázis && kártyaszámot írunk
    {
        btnRed.addEventListener("click", abort); //klikk event, meghívja az abort eseményt
        btnYellow.addEventListener("click", cancel); //klikk->meghívja a cancel eseményt
        numberArray.push(n);//beteszi a számot a tömb végére
        if(numberArray.length == 4 || numberArray.length == 9 || numberArray.length == 14)
        {
            numberArray.push(" "); // beszúr egy szóköszt 4-számot követően a tömb végére
        }
        numString = numberArray.join("").toString(); //összevonja a tömb elemeit és átalakítja sztringgé

        if(numberArray.length == 19)
        {
            greenText.innerHTML = "Next";// megjelenik a submit, 1. lekérhetjük egy HTML elem tartalmát, illetve 2.módosíthatjuk egy HTML elem tartalmát.
            btnGreen.addEventListener("click", next); //eseményfigyelő, gomlenyomás határsára submit event
            isCardCode = false;
        }
        textBox.innerHTML = numString; //számok kiírására
        yellowText.innerHTML = "Delete"; //kiíratás
        redText.innerHTML = "Abort";
    }

    if(firstPhase && isPinCode) //elsőfázis && pin kód (submit-ban van true-ra állítva)
    {
        redText.innerHTML = "Quit";
        btnRed.addEventListener("click", refresh);
        //if(responseMessage.includes("New"))
        //{
                        numberArray.push(n)
                        if(numberArray.length == 4)
                        {
                            pinCode = numberArray.join("").toString();
                            //let request = new XMLHttpRequest();
                            /*request.onreadystatechange = function() //onreadystatechange , mit jelent, meg magába a function()
                            {
                                if (this.readyState == 4)
                                {*/
                                    //messageBox.innerHTML = this.responseText; //responsText mit csinál?
                                    //console.log(this.responseText);
                                    //console.log(this.responseText);
                           /*     }
                            };*/
                            //request.open("POST", "/newCustomer"); //itt hívja meg a @RequestMapping(path = "/cardNumber", method = RequestMethod.POST)
                            //request.send(pinCode);//elküldi a pinkódot
                            isPinCode = false;
                            firstPhase = false;
                            //messageBox.innerText = "";
                            //textBox.innerText = "";
                            setTimeout(submitLogin, 100);  // Kis késleltetés, hogy ne fusson párhuzamosan messageBox: undefined miatt
                            //submitLogin();
                        }

          //  customer = false; // átmegy a következő metódusba
        //}

        /*else
        {
            numberArray.push(n)
            if(numberArray.length == 4)
            {
                pinCode = numberArray.join("").toString();
                /*let request = new XMLHttpRequest();
                request.onreadystatechange = function()
                {
                    if (this.readyState == 4)
                    {
                        messageBox.innerHTML = this.responseText;
                        console.log(this.responseText);
                        console.log(this.responseText);
                    }
                };
                request.open("POST", "/pinCode");
                request.send(pinCode);
                isPinCode = false;
                firstPhase = false;
            }
        }*/
        numString = numberArray.join("").toString().replaceAll(/[0-p]/ig, "*"); //kicseréli a join tömb számait *-ra
        textBox.innerHTML = numString;
        //console.log(numString);

    }

    if(!firstPhase && customer && !isDeposit && !isWithdraw) {
        messageBox.innerHTML = responseMessage;
        if(!responseMessage.includes("Pin")) { //Please enter pin code.
            textBox.innerHTML = "Please choose what to do next";
            greenText.innerHTML = "Deposit";
            btnGreen.addEventListener("click", deposit);
            yellowText.innerHTML = "Withdraw"
            btnYellow.addEventListener("click", withdraw);
            redText.innerHTML = "Quit";
            btnRed.addEventListener("click", refresh);
            numberArray = [];
            responseMessage = "";
        }
        else {
            greenText.innerHTML = "";
            yellowText.innerHTML = ""
            redText.innerHTML = "Quit";
            btnRed.addEventListener("click", refresh);
        }

    }

    if(!firstPhase && !customer && !isDeposit && !isWithdraw) {
            messageBox.innerHTML = responseMessage;
            textBox.innerHTML = "";
            greenText.innerHTML = "";
            yellowText.innerHTML = ""
            redText.innerHTML = "Quit";
            btnRed.addEventListener("click", refresh);

    }

    if(isDeposit) {
        numberArray.push(n);
        numString = numberArray.join("").toString();
        textBox.innerHTML = numString;
        amountOfMoney = numString;

        btnGreen.addEventListener("click", submitDeposit);
    }

    if(isWithdraw) {
        numberArray.push(n);
        numString = numberArray.join("").toString();
        textBox.innerHTML = numString;
        amountOfMoney = numString;

        btnGreen.addEventListener("click", submitWithdraw);
    }

}

function next() {
    cardNumber = numString.replace(/\s/g, "");
    /*let request = new XMLHttpRequest();
    request.onreadystatechange = function() {
                    if (this.readyState == 4) {
                        messageBox.innerHTML = this.responseText;
                        responseMessage = this.responseText;
                        console.log(this.responseText);
                        console.log(this.responseText);
                    }
                };
    request.open("POST", "/cardNumber");
    request.send(cardNumber);*/
    yellowText.innerHTML = "";
    greenText.innerHTML = "";
    redText.innerHTML = "";
    isPinCode = true;
    numberArray = [];
    numString = numberArray.toString();
    textBox.innerHTML = numString;
    btnGreen.removeEventListener("click", next);
    messageBox.innerHTML = "Please enter pin code";
}

function submitLogin() {
    document.getElementById("usernameField").value = cardNumber;
    document.getElementById("passwordField").value = pinCode;

    //  Üzenetek törlése, hogy ne villanjon "undefined"
        document.getElementById("messageBox").innerText = "";
        document.getElementById("textBox").innerText = "";

    document.getElementById("loginForm").submit();

}

function cancel() {
    if(numberArray.length <= 19) {
        greenText.innerHTML = "";
        btnGreen.removeEventListener("click", next);
    }

    if(isCardCode || numberArray.length == 19) {
        if(numberArray.length == 19) {
            isCardCode = true;
        }
        if(numberArray[numberArray.length-1] == " ") {
            numberArray.pop();
        }
        numberArray.pop();
        numString = numberArray.join("").toString();
        textBox.innerHTML = numString;
    }
}

function abort() {
    numberArray = [];
    isCardCode = true;
    numString = numberArray.join("").toString();
    textBox.innerHTML = numString;
}

function refresh() { //a Refresh gombhoz hasonlóan újratölti az aktuális URL-t
    window.location.reload();
}

function withdraw() {
    textBox.innerHTML = "";
    greenText.innerHTML = "Submit";
    yellowText.innerHTML = "";
    btnRed.addEventListener("click", refresh);
    messageBox.innerHTML = "Please enter the amount of money you would like to withdraw";
    isWithdraw = true;
    btnGreen.removeEventListener("click", deposit);
}

function deposit() {
    textBox.innerHTML = "";
    greenText.innerHTML = "Submit";
    yellowText.innerHTML = "";
    btnRed.addEventListener("click", refresh);
    messageBox.innerHTML = "Please enter the amount of money you would like to deposit";
    isDeposit = true;
    btnGreen.removeEventListener("click", deposit);
}

function submitDeposit() {
    textBox.innerHTML = "Thank you for choosing us";
    greenText.innerHTML = "";
    btnGreen.removeEventListener("click", submitDeposit);
    btnYellow.removeEventListener("click", withdraw);
        let request = new XMLHttpRequest();
        request.onreadystatechange = function() {
                        if (this.readyState == 4) {
                            messageBox.innerHTML = this.responseText;
                            responseMessage = this.responseText;
                            console.log(this.responseText);
                            console.log(this.responseText);
                        }
                    };
        request.open("POST", "/deposit");
        request.send(amountOfMoney);
}

function submitWithdraw() {
    textBox.innerHTML = "Thank you for choosing us";
    greenText.innerHTML = "";
    btnGreen.removeEventListener("click", submitWithdraw);
    btnYellow.removeEventListener("click", withdraw);
        let request = new XMLHttpRequest();
        request.onreadystatechange = function() {
                        if (this.readyState == 4) {
                            messageBox.innerHTML = this.responseText;
                            responseMessage = this.responseText;
                            console.log(this.responseText);
                            console.log(this.responseText);
                        }
                    };
        request.open("POST", "/withdraw");
        request.send(amountOfMoney);
}


// Hibás login kezelése Spring Security felől
document.addEventListener("DOMContentLoaded", function () { // Ez egy biztonságos módja annak, hogy csak akkor manipuláljunk DOM elemeket (pl. messageBox), ha az egész HTML már betöltődött. Amikor a dokumentum teljesen betöltődött, akkor hajtsd végre a belső function() tartalmát.
    const params = new URLSearchParams(window.location.search); // lekéri az URL-ből a query paramétereket (pl. ?error)
    if (params.has("error")) {
        messageBox.innerText = "Incorrect username or password.";

        // 3 másodperc után eltüntetjük az ?error paramétert az URL-ből
       setTimeout(function () {
                   window.location.href = window.location.origin + "/";
               }, 3000);
    }
});

// Szerver wake-up hívás az oldal teljes betöltődése után
window.addEventListener('load', () => {
    fetch('/api/ping')
        .then(response => response.text())
        .then(data => console.log('Wakeup response:', data))
        .catch(error => console.error('Wakeup error:', error));
});