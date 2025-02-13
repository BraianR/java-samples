// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


// [START classroom_create_course]
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Collections;

/* Class to demonstrate the use of Classroom Create Course API */
public class CreateCourse {
    /**
     * Creates a course
     *
     * @return newly created course
     * @throws IOException - if credentials file not found.
     */
    public static Course createCourse() throws IOException {
        /* Load pre-authorized user credentials from the environment.
           TODO(developer) - See https://developers.google.com/identity for
            guides on implementing OAuth2 for your application. */
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(Collections.singleton(ClassroomScopes.CLASSROOM_COURSES));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                credentials);

        // Create the classroom API client
        Classroom service = new Classroom.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Classroom samples")
                .build();

        Course course = null;
        try {
            // Adding a new course with description
            course = new Course()
                    .setName("10th Grade Biology")
                    .setSection("Period 2")
                    .setDescriptionHeading("Welcome to 10th Grade Biology")
                    .setDescription("We'll be learning about about the structure of living creatures "
                            + "from a combination of textbooks, guest lectures, and lab work. Expect "
                            + "to be excited!")
                    .setRoom("301")
                    .setOwnerId("me")
                    .setCourseState("PROVISIONED");
            course = service.courses().create(course).execute();
            // Prints the new created course Id and name
            System.out.printf("Course created: %s (%s)\n", course.getName(), course.getId());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 400) {
                System.err.println("Unable to create course, ownerId not specified.\n");
            } else {
                throw e;
            }
        }
        return course;
    }
}
// [END classroom_create_course]