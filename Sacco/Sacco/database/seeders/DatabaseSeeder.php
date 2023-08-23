<?php

namespace Database\Seeders;
use Database\Seeders\MembersTableSeeder;
use Database\Seeders\DepositSeeder;
use Illuminate\Database\Seeder;
// use App\Models\User;


class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
          User::factory()->create([
             'name' => 'Admin',
             'email' => 'admin@material.com',
             'password' => ('secret')
         ]);

        $this->call(MembersTableSeeder::class);
        $this->call(DepositSeeder::class);
    }
}
