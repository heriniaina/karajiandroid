/*
Yaaic - Yet Another Android IRC Client

Copyright 2009-2010 Sebastian Kaspari

This file is part of Yaaic.

Yaaic is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Yaaic is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Yaaic.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.yaaic.test.scenario;

import org.yaaic.R;
import org.yaaic.activity.ConversationActivity;
import org.yaaic.activity.ServersActivity;

import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

/**
 * Scenario helper for performing common actions
 *
 * @author Sebastian Kaspari <sebastian@yaaic.org>
 */
public class ScenarioHelper
{
	private Solo solo;

	public ScenarioHelper(Solo solo)
	{
		this.solo = solo;
	}

	/**
	 * Create a test server (RobotiumTest)
	 *
	 * Starting Point: ServersActivity
	 * Ending Point:   ServersActivity
	 *
	 * @param solo
	 */
	public void createTestServer()
	{
		if (!solo.searchText("RobotiumTest")) {
			solo.assertCurrentActivity("Starting at ServersActivity", "ServersActivity");

			View view = solo.getView(R.id.add);
			assert view != null;

			solo.clickOnView(view);

			solo.waitForActivity("AddServerActivity");
			solo.assertCurrentActivity("Switched to AddServerActivity", "AddServerActivity");

			solo.enterText(0, "RobotiumTest");
			solo.enterText(1, "irc.epd-me.net");

			solo.enterText(4, "YaaicBotium");

			// Somehow robotium doesn't detect the field when using index 5
			EditText realname = (EditText) solo.getView(R.id.realname);
			solo.enterText(realname, "Robotium and Yaaic");

			solo.clickOnButton(solo.getString(R.string.server_save));

			solo.waitForActivity("ServersActivity", 2000);
		}
	}

	/**
	 * Connect to the test server
	 *
	 * Starting Point: ServersActivity
	 * Ending Point:   ConversationsActivity
	 *
	 * @param solo
	 */
	public void connectToServer()
	{
		solo.clickOnText("RobotiumTest");

		solo.waitForActivity("ConversationActivity", 3000);
		solo.assertCurrentActivity("Assert is ConversationActivity", ConversationActivity.class);

		solo.waitForText(solo.getString(R.string.message_login_done));
	}

	/**
	 * Disconnect from the test server
	 *
	 * Starting Point: ConversationsActivity
	 * Ending Point:   ServersActivity
	 *
	 * @param solo
	 */
	public void disconnectFromServer()
	{
		// Go back
		solo.goBack();
		solo.waitForActivity("ServersActivity", 2000);
		solo.assertCurrentActivity("Assert is ServersActivity", ServersActivity.class);

		// Disconnect
		solo.clickLongOnText("RobotiumTest");
		solo.clickOnText("Disconnect");

		solo.waitForActivity("ServersActivity", 1000);
		solo.assertCurrentActivity("Assert is ServersActivity", ServersActivity.class);
	}

	/**
	 * Delete the test server (RobotiumTest)
	 *
	 * Starting Point: ServersActivity
	 * Ending Point:   ServersActivity
	 *
	 * @param solo
	 */
	public void deleteTestServer()
	{
		if (solo.searchText("RobotiumTest")) {
			// Delete server again
			solo.clickLongOnText("RobotiumTest");
			solo.clickOnText("Delete");

			solo.waitForActivity("ServersActivity", 1000);
			solo.assertCurrentActivity("Assert is ServersActivity", ServersActivity.class);
		}
	}

	/**
	 * Join the test channel
	 *
	 * Starting Point: ConversationsActivity
	 * Ending Point:   ConversationsActivity
	 *
	 * @param solo
	 */
	public void joinTestChannel()
	{
		// Send join command
		send("/j #yaaic-test");

		// Wait for channel joined
		solo.waitForText("#yaaic-test");
	}

	/**
	 * Send a message via the command line
	 *
	 * Starting Point: ConversationsActivity
	 * Ending Point:   ConversationsActivity
	 *
	 * @param solo
	 * @param message
	 */
	public void send(String message)
	{
		solo.enterText(0, message);
		solo.sendKey(Solo.ENTER);
	}
}
