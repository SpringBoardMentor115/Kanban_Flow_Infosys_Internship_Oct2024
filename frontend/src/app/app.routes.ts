import { Routes } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { MyKanbanComponent } from './components/my-kanban/my-kanban.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { MyProjectTeamComponent } from './components/my-project-teams/my-project-teams.component';
import { DemoAngularMaterialModule } from './DemoAngularMaterialModule';
import { UpdateTaskComponent } from './components/update-task/update-task.component';
import { CreateProjectComponent } from './components/create-project/create-project.component';
import { AddMemberComponent } from './components/add-member/add-member.component';
export const routes: Routes = [
    {
        path: '',
        redirectTo: 'My-Kanban',
        pathMatch: 'full'
    },
    {
        path: 'My-Kanban',
        component: MyKanbanComponent
    },
    {
        path: 'register-user',
        component: RegistrationComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'forgot-password',
        component: ForgotPasswordComponent
    },
    {
        path: 'profile',
        component: ProfileComponent
    },
    {
        path: 'sidebar',
        component: SidebarComponent
    },
    {
        path: 'my-project-teams',
        component: MyProjectTeamComponent
    },
    {
        path: 'DemoAngularMaterialModule',
        component: DemoAngularMaterialModule
    },
    {
        path: 'update-task',
        component: UpdateTaskComponent
    },
    {
        path: 'create-project',
        component: CreateProjectComponent
    },
    {
        path: 'add-member',
        component: AddMemberComponent
    }
];
