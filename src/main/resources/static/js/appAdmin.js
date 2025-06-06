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
let isRegister = false;
let isDelete = false;
let isLogout = true;
let cardNumber;
let pinCode;
let responseMessage;
let amountOfMoney;


textBox.innerHTML = "Please choose what to do next";
                greenText.innerHTML = "Registration";
                btnGreen.addEventListener("click", register);
                yellowText.innerHTML = "Delete user"
                btnYellow.addEventListener("click", deleteUser);
                redText.innerHTML = "Logout";
                btnRed.addEventListener("click", logout);
                numberArray = [];
                responseMessage = "";


function btnPush(n) //HTML-ben hívódik meg: onclick 1-9
{
    if (isLogout) {
        if(firstPhase && isCardCode) //első fázis && kártyaszámot írunk
        {
            resetEventListeners();
            btnRed.addEventListener("click", abort); //klikk event, meghívja az abort eseményt
            btnYellow.addEventListener("click", cancel); //klikk->meghívja a cancel eseményt
            numberArray.push(n);//beteszi a számot a tömb végére

            if(numberArray.length == 4 || numberArray.length == 9 || numberArray.length == 14)
            {
                console.log("numberArray.length: " + numberArray.length);
                numberArray.push(" "); // beszúr egy szóköszt 4-számot követően a tömb végére
            }
            numString = numberArray.join("").toString(); //összevonja a tömb elemeit és átalakítja sztringgé

            if(numberArray.length == 19)
            {
                if(isDelete) {
                 greenText.innerHTML = "Submit";
                 btnGreen.addEventListener("click", submitDeleteUser);
                }
                else {
                greenText.innerHTML = "Next";
                btnGreen.addEventListener("click", next);
                }
                // megjelenik a submit, 1. lekérhetjük egy HTML elem tartalmát, illetve 2.módosíthatjuk egy HTML elem tartalmát.
                 //eseményfigyelő, gomlenyomás határsára submit event
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
                    //console.log(pinCode);
                    isPinCode = false;
                    firstPhase = false;
                    messageBox.innerText = "";
                    textBox.innerText = "";
                    setTimeout(submitRegister, 100);  // Kis késleltetés, hogy ne fusson párhuzamosan messageBox: undefined miatt
                    //submitLogin();
                }
                numString = numberArray.join("").toString().replaceAll(/[0-p]/ig, "*"); //kicseréli a oin tömb számait *-ra
                textBox.innerHTML = numString;
                //console.log(numString);
        }
    }
}

function next() {
    cardNumber = numString.replace(/\s/g, "");
    yellowText.innerHTML = "";
    greenText.innerHTML = "";
    redText.innerHTML = "";
    isPinCode = true;
    numberArray = [];
    numString = numberArray.toString();
    textBox.innerHTML = numString;
    btnGreen.removeEventListener("click", next);
    messageBox.innerHTML = "Please enter new pin code";
    //console.log(cardNumber);
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

function showHomeMenu() {
    // Minden eseménykezelő törlése
    //resetEventListeners();

    // Szövegdobozok, üzenetek alapállapotra állítása
    textBox.innerHTML = "Please choose what to do next";
    messageBox.innerHTML = "";

    greenText.innerHTML = "Registration";
    yellowText.innerHTML = "Delete user";
    redText.innerHTML = "Logout";

    // Állapotváltozók visszaállítása
    isRegister = false;
    isDelete = false;
    firstPhase = true;
    isPinCode = false;
    isCardCode = true;

    btnGreen.addEventListener("click", register);
    btnYellow.addEventListener("click", deleteUser);
    btnRed.addEventListener("click", logout);

    numberArray = [];
    responseMessage = "";
}

function register() {

    resetEventListeners();
    textBox.innerHTML = "";
    greenText.innerHTML = "";
    yellowText.innerHTML = "";
    btnRed.addEventListener("click", refresh);
    messageBox.innerHTML = "Please enter new card number"; //HTML-ben deklarálva
    //isRegister = true;
    btnGreen.removeEventListener("click", register);
    isRegister = true;
    isDelete = false;
}

function deleteUser() {
    textBox.innerHTML = "";
    greenText.innerHTML = "";
    yellowText.innerHTML = "";
    btnRed.addEventListener("click", refresh);
    messageBox.innerHTML = "Please enter the card number"; //HTML-ben deklarálva
    //isRegister = true;
    btnGreen.removeEventListener("click", deleteUser);
    isRegister = false;
    isDelete = true; //máshol false-ra állítani!!!
}

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function submitRegister() {
    //console.log("register()");
    //const amount = parseInt(document.getElementById("amountField").value);
    //const amount = amountOfMoney;
    //console.log(amount);
    fetch('/admin/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
                                    [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
            userId: cardNumber,
            plainPassword: pinCode,
            initialAmount: 0,
            roles: ['ROLE_EMPLOYEE']
          })
      })
      .then(response => response.text())
      .then(data => {
              document.getElementById("messageBox").innerText = data;

        // 4 másodperc múlva visszaállítjuk az alapüzenetet
        setTimeout(() => {
            window.location.reload(); // újratölti (frissíti) az aktuális weboldalt.
        }, 4000);

      })
      .catch(error => console.error('Hiba:', error));

      document.getElementById("textBox").innerHTML = "";
}

function submitDeleteUser() {
    cardNumber = numString.replace(/\s/g, "");
    //const amount = parseInt(document.getElementById("amountField").value);
    //const amount = amountOfMoney;
    console.log("cardNumber: " + cardNumber);
    fetch('/admin/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
                                    [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({
                    'accountNumber': cardNumber
                })
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById("messageBox").innerText = data;
            setTimeout(showHomeMenu, 3000);
    });
    resetEventListeners();
    textBox.innerHTML = "";
    redText.innerHTML = "";
    yellowText.innerHTML = "";
    greenText.innerHTML = "";
}

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

            // szám gombok inaktivitása
            isLogout = false;

            textBox.innerText = "";
            greenText.innerText = "";
            btnGreen.removeEventListener("click", register);
            yellowText.innerText = "";
            btnYellow.removeEventListener("click", deleteUser);
            redText.innerText = "";
            btnRed.removeEventListener("click", logout);


            // 3 másodperc múlva navigálunk
            setTimeout(() => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    window.location.href = "/";
                }

                // szám gombok aktiválása
                isLogout = true;
            }, 3000);
        }).catch(error => {
            console.error("There was an error on exit:", error);
        });
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