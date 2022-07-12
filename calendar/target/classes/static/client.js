const subscribeButton = document.getElementById('subscribeButton');
const unsubscribeButton = document.getElementById('unsubscribeButton');

const notificationOutput = document.getElementById('notification');

const email = document.getElementById('email');
const alert = document.getElementById('alert');
const alert2 = document.getElementById('alert2');

if ("serviceWorker" in navigator) {
	try {
		checkSubscription();
		init();
	} catch (e) {
		console.error('error init(): ' + e);
	}

	subscribeButton.addEventListener('click', () => {
		subscribe().catch(e => {
			if (Notification.permission === 'denied') {
				console.warn('Permission for notifications was denied');
			} else {
				console.error('error subscribe(): ' + e);
			}
		});
	});

	unsubscribeButton.addEventListener('click', () => {
		unsubscribe().catch(e => console.error('error unsubscribe(): ' + e));
	});
}


async function checkSubscription() {
	const registration = await navigator.serviceWorker.ready;
	const subscription = await registration.pushManager.getSubscription();
	if (subscription) {
		//add users
		const response = await fetch("/users/isSubscribed", {
			method: 'POST',
			body: JSON.stringify({ endpoint: subscription.endpoint }),
			headers: {
				"content-type": "application/json"
			}
		});
		const subscribed = await response.json();

		if (subscribed) {
			subscribeButton.disabled = true;
			unsubscribeButton.disabled = false;
			email.disabled = true;
			alert.style.display = "none";
		}

		return subscribed;
	}

	return false;
}

async function init() {
	//add notifications
	fetch('/notifications/publicSigningKey')
		.then(response => response.arrayBuffer())
		.then(key => this.publicSigningKey = key)
		.finally(() => console.info('Application Server Public Key fetched from the server'));

	await navigator.serviceWorker.register("/sw.js", {
		scope: "/"
	});

	await navigator.serviceWorker.ready;
	console.info('Service Worker has been installed and is ready');
	navigator.serviceWorker.addEventListener('message', event => displayLastMessages());

	displayLastMessages();


}

function renderEvent(event) {
	event = event || {};
	return `
        <div class="event">
            <h2>${event.title}</h2>
            <p>${event.description}</p>
        </div>
    `;
}


function displayLastMessages() {
	caches.open('data').then(dataCache => {
		dataCache.match('notification')
			.then(response => response ? response.text() : '')
			.then(eventText => {
				if(eventText) {
					const event = JSON.parse(eventText); 
					notificationOutput.innerHTML = "";
					notificationOutput.innerHTML = renderEvent(event);
				}
			});

	});
}

async function unsubscribe() {
	const registration = await navigator.serviceWorker.ready;
	const subscription = await registration.pushManager.getSubscription();
	if (subscription) {
		const successful = await subscription.unsubscribe();
		if (successful) {
			console.info('Unsubscription successful');

			await fetch("/users/unsubscribe/" + email.value, {
				method: 'POST',
				body: JSON.stringify({ endpoint: subscription.endpoint }),
				headers: {
					"content-type": "application/json"
				}
			});

			console.info('Unsubscription info sent to the server');

			subscribeButton.disabled = false;
			unsubscribeButton.disabled = true;
			email.disabled = false;

			await caches.open('data').then(dataCache => dataCache.delete('notification'));
			notificationOutput.innerHTML = "";

		}
		else {
			console.error('Unsubscription failed');
		}
	}
}

async function subscribe() {
	if (email.value === "") {
		alert.style.display = "block";
		return;
	} else {
		alert.style.display = "none";
	}
	const registration = await navigator.serviceWorker.ready;






	if (Notification.permission === 'denied') {
		await Notification.requestPermission();
	}

	const subscription = await registration.pushManager.subscribe({
		userVisibleOnly: true,
		applicationServerKey: this.publicSigningKey
	});

	console.info(`Subscribed to Push Service: ${subscription.endpoint}`);

	const res = await fetch("/users/subscribe/" + email.value, {
		method: 'POST',
		body: JSON.stringify(subscription),
		headers: {
			"content-type": "application/json"
		}
	});
	console.info('Subscription info sent to the server');
	
	if(res.status === 200) {
		alert2.style.display = "none"; // hide the last error from the server
		subscribeButton.disabled = true;
		unsubscribeButton.disabled = false;
		email.disabled = true;
	} else {
		// show the error message in the client
		const message = await res.text();
		alert2.innerText = message;
		alert2.style.display = "block";
		email.value = "";
	}
}
