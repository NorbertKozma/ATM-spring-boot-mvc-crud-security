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

// messageBox.innerHTML = "Please enter card number"; //HTML-ben deklarálva

                textBox.innerHTML = "Please choose what to do next";
                greenText.innerHTML = "Deposit";
                btnGreen.addEventListener("click", deposit);
                yellowText.innerHTML = "Withdraw"
                btnYellow.addEventListener("click", withdraw);
                redText.innerHTML = "Logout";
                btnRed.addEventListener("click", logout);
                numberArray = [];
                responseMessage = "";

function btnPush(n) //HTML-ben hívódik meg: onclick 1-9
{
    if(isDeposit) {
        numberArray.push(n);
        numString = numberArray.join("").toString();
        textBox.innerHTML = numString;
        amountOfMoney = numString;

        yellowText.innerHTML = "Delete";
        btnYellow.addEventListener("click", cancel);
        greenText.innerHTML = "Submit";
        btnGreen.addEventListener("click", submitDeposit);
    }

    if(isWithdraw) {
        numberArray.push(n);
        numString = numberArray.join("").toString();
        textBox.innerHTML = numString;
        amountOfMoney = numString;

        yellowText.innerHTML = "Delete";
        btnYellow.addEventListener("click", cancel);
        greenText.innerHTML = "Submit";
        btnGreen.addEventListener("click", submitWithdraw);
    }

}
/*
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
    request.send(cardNumber);*//*
    yellowText.innerHTML = "";
    greenText.innerHTML = "";
    redText.innerHTML = "";
    isPinCode = true;
    numberArray = [];
    numString = numberArray.toString();
    textBox.innerHTML = numString;
    btnGreen.removeEventListener("click", next);
    messageBox.innerHTML = "Please enter pin code";
}*/

function submitLogin() {
    document.getElementById("usernameField").value = cardNumber;
    document.getElementById("passwordField").value = pinCode;

    //  Üzenetek törlése, hogy ne villanjon "undefined"
        document.getElementById("messageBox").innerText = "";
        document.getElementById("textBox").innerText = "";

    document.getElementById("loginForm").submit();

}

function cancel() {
    //console.log("numberArray.length előtte: " + numberArray.length);
    /*if(numberArray.length < 3) { // kisebb, mint 3, akkor nem jelenik meg a submit
        greenText.innerHTML = "";
        btnGreen.removeEventListener("click", next);
    }*/
    numberArray.pop();
    numString = numberArray.join("").toString();
    textBox.innerHTML = numString;
    //console.log("numberArray.length utánna: " + numberArray.length);
    amountOfMoney = numString;
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

function showHomeMenu() {
    // Minden eseménykezelő törlése
    resetEventListeners();

    // Szövegdobozok, üzenetek alapállapotra állítása
    textBox.innerHTML = "Please choose what to do next";
    messageBox.innerHTML = "";

    greenText.innerHTML = "Deposit";
    yellowText.innerHTML = "Withdraw";
    redText.innerHTML = "Logout";

    // Állapotváltozók visszaállítása
    isDeposit = false;
    isWithdraw = false;

    btnGreen.addEventListener("click", deposit);
    btnYellow.addEventListener("click", withdraw);
    btnRed.addEventListener("click", logout);

    numberArray = [];
    responseMessage = "";
    amountOfMoney = "";
}

function withdraw() {

    resetEventListeners(); // minden előző eseményt törlünk

    textBox.innerHTML = "";
    greenText.innerHTML = "";
    yellowText.innerHTML = "";
    redText.innerHTML = "Quit";
    btnRed.addEventListener("click", refresh);
    messageBox.innerHTML = "Please enter the amount of money you would like to withdraw";
    isWithdraw = true;
    isDeposit = false;

}

function deposit() {

    resetEventListeners(); // minden előző eseményt törlünk

    textBox.innerHTML = "";
    greenText.innerHTML = "";
    yellowText.innerHTML = "";
    redText.innerHTML = "Quit";
    btnRed.addEventListener("click", showHomeMenu);
    messageBox.innerHTML = "Please enter the amount of money you would like to deposit";
    isDeposit = true;
    isWithdraw = false;

}
// CSRF token lekérése
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function submitDeposit() {
    //btnRed.removeEventListener("click", refresh);
    //console.log("submitDeposit()");
    //const amount = parseInt(document.getElementById("amountField").value);
    resetEventListeners();
    const amount = amountOfMoney;
    //console.log(amount);
    fetch('/users/deposit', { // HTTP POST kérést indít a /users/deposit végpontra. Ez a szerver API-ja, ami feldolgozza a befizetést.
        method: 'POST', // A HTTP metódus POST, tehát adatot küld a szervernek.
        headers: {
            'Content-Type': 'application/json', // JSON adatküldés
                                    [csrfHeader]: csrfToken // Hozzáadja a CSRF tokent, hogy a Spring Security ne dobjon hibát a védelem miatt.
        },
        body: JSON.stringify({amount: amount}) // A amount értéket JSON objektummá alakítja, amit a szerver értelmezni tud ({ "amount": 5000 } pl.).
            })
            .then(async response => { // A fetch válaszát feldolgozza. async, mert lehet, hogy JSON választ is kell olvasnia.
                if (!response.ok) { // Ha a szerver hibát küld (pl. 400 vagy 500-as státuszkód)
                    const data = await response.json(); // Kiolvassa a hibaválaszt JSON-ként (pl. egy validációs hibaüzenet).
                    const errorMessage = data.errors?.[0]?.defaultMessage || "Unknown error."; // Kinyeri az első hibaüzenetet, ha van (optional chaining segítségével). Ha nincs, akkor "Unknown error.".
                    document.getElementById("messageBox").innerText = errorMessage; // Kiírja a hibaüzenetet az oldalra egy HTML elembe.
                    setTimeout(showHomeMenu, 3000); // 3 másodperc múlva visszanavigál a kezdőképernyőre.
                    return; //nem fut le a sikeres ágra tartozó kód.
                }
                // Csak ha sikeres a válasz
                const successMessage = await response.text(); // Ha nincs hiba, akkor sima szöveges választ vár el (pl. "Sikeres befizetés").
                document.getElementById("messageBox").innerText = successMessage; // Kiírja a sikerválaszt az oldalra.
                setTimeout(logout, 3000); // 3 másodperc múlva kijelentkezteti a felhasználót.
            /*.then(response => response.text())
            .then(data => {
                document.getElementById("messageBox").innerText = data;
                setTimeout(logout, 3000);*/
    });
    textBox.innerHTML = ""; // Kiüríti a különböző színes szövegeket
    redText.innerHTML = "";
    yellowText.innerHTML = "";
    greenText.innerHTML = "";

}

/*
function submitDeposit() {
    console.log("submitDeposit()");
    //const amount = parseInt(document.getElementById("amountField").value);
    const amount = amountOfMoney;
    console.log(amount);
    fetch('/users/deposit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
                                    [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({
                    'amount': amount
                })
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById("messageBox").innerText = data;
    });
    textBox.innerHTML = "";
}
*/

function submitWithdraw() {
    resetEventListeners();
    //const amount = parseInt(document.getElementById("amountField").value);
    const amount = amountOfMoney;
    console.log(amount);
    fetch('/users/withdraw', {
        method: 'POST',
        headers: {
                    'Content-Type': 'application/json',
                                            [csrfHeader]: csrfToken
                },
                body: JSON.stringify({amount: amount})
            })
            .then(async response => {
                if (!response.ok) {
                    const data = await response.json();
                    const errorMessage = data.errors?.[0]?.defaultMessage || "Unknown error."; //optional chaining, pl. "defaultMessage": "Must be greater than or equal to 1000", ha response.text() lett volna akkor nem jelenik meg, csaz az egész hibaüzenet
                    document.getElementById("messageBox").innerText = errorMessage;
                    setTimeout(showHomeMenu, 3000);
                    return; //nem fut le a sikeres ágra tartozó kód.
                }
                const successMessage = await response.text();
                document.getElementById("messageBox").innerText = successMessage;
                setTimeout(logout, 3000);
    });
    textBox.innerHTML = "";
    redText.innerHTML = "";
    yellowText.innerHTML = "";
    greenText.innerHTML = "";
}

/*
function submitWithdraw() {
    resetEventListeners();
    //const amount = parseInt(document.getElementById("amountField").value);
    const amount = amountOfMoney;
    console.log(amount);
    fetch('/users/withdraw', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
                                    [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({ //, ha a Controller: public ResponseEntity<String> withdrawal(@Valid @RequestParam("amount") String theAmount, Model theModel, Principal principal) {
                    'amount': amount
                })
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById("messageBox").innerText = data;
                setTimeout(logout, 3000);
    });
    textBox.innerHTML = "";
    redText.innerHTML = "";
    yellowText.innerHTML = "";
    greenText.innerHTML = "";
}
*/

function logout() {

    fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken
            }
        }).then(response => {
            // Megjelenítjük az üzenetet
            document.getElementById("messageBox").innerText = "Successful logout";

            textBox.innerHTML = "";
            greenText.innerText = "";
            btnGreen.removeEventListener("click", deposit);
            yellowText.innerText = "";
            btnYellow.removeEventListener("click", withdraw);
            redText.innerText = "";
            btnRed.removeEventListener("click", logout);


            // 3 másodperc múlva navigálunk
            setTimeout(() => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    window.location.href = "/";
                }
            }, 3000);
        }).catch(error => {
            console.error("There was an error on exit:", error);
        });


    /*fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        }
    }).then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        } else {
            window.location.href = "/";
        }
    }).catch(error => {
        console.error("There was an error on exit:", error);
    }); */
}

function resetEventListeners() {
    btnGreen.replaceWith(btnGreen.cloneNode(true));
    btnYellow.replaceWith(btnYellow.cloneNode(true));
    btnRed.replaceWith(btnRed.cloneNode(true));

    // újra lekérjük a gombokat, mert a régieket lecseréltük
    btnGreen = document.getElementById("btn-green");
    btnYellow = document.getElementById("btn-yellow");
    btnRed = document.getElementById("btn-red");
}

/*
function submitDeposit() {
    textBox.innerHTML = "Thank you for choosing us";
    greenText.innerHTML = "";
    btnGreen.removeEventListener("click", submitDeposit);
    btnYellow.removeEventListener("click", withdraw);

    //document.getElementById("amountField").value = amountOfMoney;

        //  Üzenetek törlése, hogy ne villanjon "undefined"
        //    document.getElementById("messageBox").innerText = "";
        //    document.getElementById("textBox").innerText = "";

        //document.getElementById("amountForm").submit();

        messageBox.innerHTML = this.responseText;
        responseMessage = this.responseText;
        //let request = new XMLHttpRequest();
        //request.onreadystatechange = function() {
                        //if (this.readyState == 4) {
                            messageBox.innerHTML = this.responseText;
                            responseMessage = this.responseText;
                            console.log(this.responseText);
                            console.log(this.responseText);
                        //}
                    //};
        //request.open("POST", "/deposit");
        //request.send(amountOfMoney);
}
*/
/*
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
*/